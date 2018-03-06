package io.sikorka.android.ui.contracts.deploydetectorcontract

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.sikorka.android.R
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.core.contracts.model.DetectorContractData
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.contracts.DeployContractCodes
import io.sikorka.android.ui.contracts.dialog.ConfirmDeployDialog
import io.sikorka.android.ui.dialogs.createDialog
import io.sikorka.android.ui.gasselectiondialog.GasSelectionDialog
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.ui.value
import io.sikorka.android.utils.getBitmapFromVectorDrawable
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__advanced_options
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__authorization_duration
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__contract_name
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__contract_tokens
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__deploy_fab
import kotlinx.android.synthetic.main.activity_deploy_detector.deploy_detector__detector_address
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class DeployDetectorActivity : BaseActivity(), DeployDetectorView {

  @Inject
  lateinit var presenter: DeployDetectorPresenter

  private lateinit var scope: Scope

  private var myMarker: Marker? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(DeployDetectorModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieSupportActivityModule(this))
    super.onCreate(savedInstanceState)

    Toothpick.inject(this, scope)
    setContentView(R.layout.activity_deploy_detector)

    setupToolbar(R.string.deploy_detector__title)

    deploy_detector__detector_address.text = address

    val mapFragment =
      supportFragmentManager.findFragmentById(R.id.deploy_detector__map) as SupportMapFragment
    mapFragment.getMapAsync {

      val me = LatLng(latitude, longitude)

      val bitmap = getBitmapFromVectorDrawable(R.drawable.ic_person_pin_circle_black_24dp)
      val icon = BitmapDescriptorFactory.fromBitmap(bitmap)

      val position = CameraPosition.builder()
        .target(me)
        .zoom(10f)
        .bearing(0.0f)
        .tilt(0.0f)
        .build()

      myMarker = it.addMarker(MarkerOptions().position(me).title("Me").icon(icon))
      it.animateCamera(CameraUpdateFactory.newCameraPosition(position), null)
      it.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    deploy_detector__deploy_fab.setOnClickListener {
      deploy_detector__authorization_duration.error = null
      if (authorizationDuration == 0) {
        deploy_detector__authorization_duration.error =
          getString(R.string.deploy_detector__authorization_duration_not_set)
      }

      deploy_detector__contract_tokens.error = null
      val supply = deploy_detector__contract_tokens.editText?.value()?.toLong() ?: 0

      if (supply == 0L) {
        deploy_detector__contract_tokens.error =
          getString(R.string.deploy_detector__token_supply_empty)
        return@setOnClickListener
      }

      if (deploy_detector__advanced_options.isChecked) {
        presenter.prepareGasSelection()
      } else {
        presenter.prepareDeployWithDefaults()
      }
    }
    presenter.attach(this)
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    super.onDestroy()
  }

  override fun showGasDialog(gas: ContractGas) {
    val dialog = GasSelectionDialog.create(supportFragmentManager, gas) {
      requestDeployAuthorization(it)
    }
    dialog.show()
  }

  override fun showError(message: String) {
    Snackbar.make(deploy_detector__detector_address, message, Snackbar.LENGTH_SHORT).show()
  }

  override fun showError(code: Int) {
    when (code) {
      DeployContractCodes.NO_GAS_PREFERENCES -> {
        Snackbar.make(
          deploy_detector__detector_address,
          R.string.deploy_contract__no_gas_preferences,
          Snackbar.LENGTH_SHORT
        ).show()
      }
      else -> {
        Snackbar.make(
          deploy_detector__detector_address,
          "code $code",
          Snackbar.LENGTH_SHORT
        ).show()
      }
    }
  }

  override fun complete(hex: String) {
    createDialog(
      R.string.contract_deployment__transaction_sent_title,
      getString(R.string.contract_deployment__transaction_sent_content, hex)
    ) {
      MainActivity.start(this)
    }
  }

  override fun requestDeployAuthorization(gas: ContractGas) {
    val supply = deploy_detector__contract_tokens.editText?.value()?.toLong() ?: 0
    val dialog = ConfirmDeployDialog.create(supportFragmentManager, gas) { passphrase ->
      val data = DetectorContractData(
        name,
        gas,
        address,
        authorizationDuration,
        latitude,
        longitude,
        supply
      )
      presenter.deployContract(passphrase, data)
    }
    dialog.show()
  }

  private val address: String
    get() = intent?.getStringExtra(DETECTOR_ADDRESS) ?: fail("expected a non null value")

  private val latitude: Double
    get() = intent?.getDoubleExtra(LATITUDE, 0.0) ?: fail("latitude was null")

  private val longitude: Double
    get() = intent?.getDoubleExtra(LONGITUDE, 0.0) ?: fail("longitude was null")

  private val authorizationDuration: Int
    get() {
      val editText =
        deploy_detector__authorization_duration.editText ?: fail("there was no edittext")
      val value = editText.value()
      return if (value.isBlank()) {
        0
      } else {
        value.toInt()
      }
    }

  private val name: String
    get() {
      val editText = deploy_detector__contract_name.editText ?: fail("null")
      return editText.value()
    }

  @javax.inject.Scope
  @Target(AnnotationTarget.CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java
    private const val DETECTOR_ADDRESS = "io.sikorka.android.extras.DETECTOR_ADDRESS"
    private const val LONGITUDE = "io.sikorka.android.extras.LONGITUDE"
    private const val LATITUDE = "io.sikorka.android.extras.LATITUDE"

    fun start(context: Context, address: String, latitute: Double, longitude: Double) {
      val intent = Intent(context, DeployDetectorActivity::class.java)
      intent.putExtra(DETECTOR_ADDRESS, address)
      intent.putExtra(LATITUDE, latitute)
      intent.putExtra(LONGITUDE, longitude)
      context.startActivity(intent)
    }
  }
}