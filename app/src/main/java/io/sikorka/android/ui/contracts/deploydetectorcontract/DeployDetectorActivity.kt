package io.sikorka.android.ui.contracts.deploydetectorcontract

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.node.contracts.DetectorContractData
import io.sikorka.android.ui.contracts.dialog.ConfirmDeployDialog
import io.sikorka.android.ui.gasselectiondialog.GasSelectionDialog
import io.sikorka.android.ui.value
import io.sikorka.android.utils.getBitmapFromVectorDrawable
import kotlinx.android.synthetic.main.activity_deploy_detector.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class DeployDetectorActivity : AppCompatActivity(), DeployDetectorView {

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

    setupActionBar()

    deploy_detector__detector_address.text = address

    val mapFragment = supportFragmentManager.findFragmentById(R.id.deploy_detector__map) as SupportMapFragment
    mapFragment.getMapAsync {

      val me = LatLng(latitude, longitude)

      val bitmap = getBitmapFromVectorDrawable(R.drawable.ic_person_pin_circle_black_24dp)
      val icon = BitmapDescriptorFactory.fromBitmap(bitmap)

      val position = CameraPosition.builder()
          .target(me)
          .zoom(16f)
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
        deploy_detector__authorization_duration.error = getString(R.string.deploy_detector__authorization_duration_not_set)
      }

      if (deploy_detector__advanced_options.isChecked) {
        presenter.prepareGasSelection()
      } else {
        presenter.prepareDeployWithDefaults()
      }
    }
  }

  override fun onDestroy() {
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    super.onDestroy()
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
  }


  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun setupActionBar() {
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }
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
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun complete(hex: String) {
   Snackbar.make(deploy_detector__detector_address, hex, Snackbar.LENGTH_SHORT).show()
  }

  override fun requestDeployAuthorization(gas: ContractGas) {
    val dialog = ConfirmDeployDialog.create(supportFragmentManager, gas) { passphrase ->
      val data = DetectorContractData(name, gas, address, authorizationDuration, latitude, longitude)
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
      val editText = deploy_detector__authorization_duration.editText ?: fail("there was no edittext")
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
