package io.sikorka.android.ui.contracts.interact

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.sikorka.android.R
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.detector.qr.QrScannerActivity
import io.sikorka.android.ui.detector.select.SupportedDetector
import io.sikorka.android.ui.detector.select.SupportedDetectors
import io.sikorka.android.ui.dialogs.loading
import io.sikorka.android.ui.dialogs.showConfirmation
import io.sikorka.android.ui.dialogs.showInfo
import io.sikorka.android.ui.dialogs.verifyPassphraseDialog
import io.sikorka.android.ui.gasselectiondialog.GasSelectionDialog
import io.sikorka.android.utils.getBitmapFromVectorDrawable
import kotlinx.android.synthetic.main.activity__contract_interact.contract_interact__contract_address
import kotlinx.android.synthetic.main.activity__contract_interact.contract_interact__verify
import kotlinx.android.synthetic.main.activity__contract_interact.interact_contract__detector_address
import kotlinx.android.synthetic.main.activity__contract_interact.interact_contract__detector_address_group
import kotlinx.android.synthetic.main.activity__contract_interact.interact_contract__interact_with_detector
import kotlinx.android.synthetic.main.activity__contract_interact.interact_contract__manual_gas_selection
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class ContractInteractActivity : BaseActivity(), ContractInteractView {

  @Inject
  lateinit var presenter: ContractInteractPresenter

  private lateinit var scope: Scope

  private var dialog: AlertDialog? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), ContractInteractModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__contract_interact)
    Toothpick.inject(this, scope)
    setupToolbar(title = intent?.getStringExtra(NAME) ?: "")

    contract_interact__verify.setOnClickListener {
      presenter.startClaimFlow()
    }

    contract_interact__contract_address.text = contractAddress

    val map =
      supportFragmentManager.findFragmentById(R.id.interact_contract__map) as SupportMapFragment
    map.getMapAsync {
      val me = intent?.getParcelableExtra<LatLng>(MY_LOCATION) ?: return@getMapAsync
      val contract = intent?.getParcelableExtra<LatLng>(CONTRACT_LOCATION) ?: return@getMapAsync

      val bitmap = getBitmapFromVectorDrawable(R.drawable.ic_person_pin_circle_black_24dp)
      val icon = BitmapDescriptorFactory.fromBitmap(bitmap)

      val position = CameraPosition.builder()
        .target(me)
        .zoom(10f)
        .bearing(0.0f)
        .tilt(0.0f)
        .build()

      it.addMarker(MarkerOptions().position(me).title("Me").icon(icon))
      it.animateCamera(CameraUpdateFactory.newCameraPosition(position), null)
      it.moveCamera(CameraUpdateFactory.newCameraPosition(position))

      val contracBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_ethereum_icon)
      val contractIcon = BitmapDescriptorFactory.fromBitmap(contracBitmap)

      val markerOptions = MarkerOptions()
        .position(contract)
        .title(contractAddress)
        .icon(contractIcon)

      it.addMarker(markerOptions)
    }

    interact_contract__interact_with_detector.adapter = DetectorSpinnerAdapter(detectors())
    interact_contract__interact_with_detector.setSelection(0)

    interact_contract__detector_address.setOnClickListener { view ->
      val addressTextView = view as TextView
      val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      clipboardManager.primaryClip = ClipData.newPlainText("Account", addressTextView.text)
    }

    contract_interact__contract_address.setOnClickListener { view ->
      val addressTextView = view as TextView
      val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      clipboardManager.primaryClip = ClipData.newPlainText("Account", addressTextView.text)
    }

    presenter.attach(this)
    presenter.load(contractAddress)
  }

  override fun startDetectorFlow() {
    startDetectorVerification()
  }

  private fun getGasSettings() {
    if (interact_contract__manual_gas_selection.isChecked) {
      presenter.prepareGasSelection()
    } else {
      Toast.makeText(this, "Not Implemented", Toast.LENGTH_SHORT).show()
    }
  }

  override fun detector(hex: String) {
    interact_contract__detector_address.text = hex
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    super.onDestroy()
  }

  override fun noDetector() {
    interact_contract__detector_address_group.isVisible = false
  }

  override fun showConfirmationResult(confirmAnswer: Boolean) {
    dialog?.dismiss()
    showInfo(
      R.string.contract_interact__success_title,
      R.string.contract_interact__success_content
    ) {
      finish()
    }
  }

  override fun update(name: String) {
    supportActionBar?.title = name
  }

  override fun showError() {
    Snackbar.make(
      contract_interact__verify,
      R.string.contract_interact__generic_error,
      Snackbar.LENGTH_LONG
    ).show()
  }

  override fun showGasSelection(gas: ContractGas) {
    val dialog = GasSelectionDialog.create(supportFragmentManager, gas) {
      presenter.cacheGas(gas)
      showMethodChoice()
    }
    dialog.show()
  }

  private fun showMethodChoice() {
    val selectDialog = methodSelectDialog(arrayOf("claimTokens")) { _ ->
      requestUnlock()
    }

    selectDialog.show()
  }

  private fun requestUnlock() {
    verifyPassphraseDialog {
      presenter.cachePassPhrase(it)
      showConfirmation(
        R.string.contract_interact__proceed_with_claiming,
        R.string.contract_interact__proceed_with_claiming_content
      ) {
        dialog = loading(
          R.string.contract_interact__claiming_tokens_title,
          R.string.contact_interact__claiming_tokens
        )
        dialog?.show()
        presenter.verify()
      }
    }
  }

  private fun startDetectorVerification() {
    val typeId = interact_contract__interact_with_detector.selectedItemId.toInt()
    when (typeId) {
      SupportedDetectors.QR_CODE -> QrScannerActivity.start(this)
      SupportedDetectors.BLUETOOTH -> Toast.makeText(
        this,
        "Not Implemented",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Timber.v("result $resultCode")
    if (requestCode == QrScannerActivity.SCANNER_RESULT && resultCode == Activity.RESULT_OK) {
      if (data == null) {
        Snackbar.make(
          contract_interact__verify,
          R.string.contract_interact__generic_error,
          Snackbar.LENGTH_LONG
        ).show()
      } else {

        presenter.cacheMessage(data.getStringExtra(QrScannerActivity.DATA))
        getGasSettings()
      }
    }
    super.onActivityResult(requestCode, resultCode, data)
  }

  private val contractAddress: String
    get() = intent?.getStringExtra(CONTRACT_ADDRESS) ?: fail("expected a non null contract address")

  private fun detectors(): List<SupportedDetector> {
    val detectors = ArrayList<SupportedDetector>()
    detectors.add(
      SupportedDetector(
        SupportedDetectors.QR_CODE,
        R.string.select_detector_type__detector_qr,
        R.drawable.ic_qr_code_24dp
      )
    )
    detectors.add(
      SupportedDetector(
        SupportedDetectors.BLUETOOTH,
        R.string.select_detector_type__detector_bluetooth,
        R.drawable.ic_bluetooth_black_24dp
      )
    )
    return detectors
  }

  companion object {
    private const val NAME = "io.sikorka.android.extras.NAME"
    private const val CONTRACT_ADDRESS = "io.sikorka.android.extras.CONTRACT_ADDRESS"
    private const val CONTRACT_LOCATION = "io.sikorka.android.extras.CONTRACT_LOCATION"
    private const val MY_LOCATION = "io.sikorka.android.extras.MY_LOCATION"

    fun start(
      context: Context,
      name: String,
      contractAddress: String,
      me: LatLng,
      contract: LatLng
    ) {
      val intent = Intent(context, ContractInteractActivity::class.java)
      intent.putExtra(NAME, name)
      intent.putExtra(CONTRACT_ADDRESS, contractAddress)
      intent.putExtra(CONTRACT_LOCATION, contract)
      intent.putExtra(MY_LOCATION, me)
      context.startActivity(intent)
    }
  }
}