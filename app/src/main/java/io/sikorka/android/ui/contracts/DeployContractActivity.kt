package io.sikorka.android.ui.contracts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.sikorka.android.R
import io.sikorka.android.core.contracts.model.ContractData
import io.sikorka.android.core.contracts.model.ContractGas
import io.sikorka.android.ui.contracts.DeployContractCodes.NO_GAS_PREFERENCES
import io.sikorka.android.ui.contracts.dialog.ConfirmDeployDialog
import io.sikorka.android.ui.dialogs.showInfo
import io.sikorka.android.ui.gasselectiondialog.GasSelectionDialog
import io.sikorka.android.ui.value
import kotlinx.android.synthetic.main.activity__deploy_contract.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class DeployContractActivity : AppCompatActivity(), DeployContractView, OnMapReadyCallback {

  @Inject lateinit var presenter: DeployContractPresenter

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), DeployContractModule())
    super.onCreate(savedInstanceState)
    Toothpick.inject(this, scope)
    setContentView(R.layout.activity__deploy_contract)
    val mapFragment = supportFragmentManager.findFragmentById(R.id.deploy_contract__map) as SupportMapFragment
    mapFragment.getMapAsync(this)
    deploy_contract__deploy_fab.setOnClickListener {
      deploy_contract__name.error = null

      if (contractName.isBlank()) {
        deploy_contract__name.error = getString(R.string.deploy_contract__specify_contract_name)
        return@setOnClickListener
      }

      deploy_contract__token_supply.error = null
      val supply = deploy_contract__token_supply.editText?.value()?.toLong() ?: 0

      if (supply == 0L) {
        deploy_contract__token_supply.error = getString(R.string.deploy_contract__token_supply_empty)
        return@setOnClickListener
      }

      if (deploy_contract__advanced_options.isChecked) {
        presenter.prepareGasSelection()
      } else {
        presenter.prepareDeployWithDefaults()
      }
    }

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }
    presenter.attach(this)
    presenter.load()
  }

  override fun showError(code: Int) {
    when (code) {
      NO_GAS_PREFERENCES -> {
        Snackbar.make(deploy_contract__deploy_fab, R.string.deploy_contract__no_gas_preferences, Snackbar.LENGTH_LONG).show()
      }
    }
  }

  override fun showGasDialog(gas: ContractGas) {
    val dialog = GasSelectionDialog.create(supportFragmentManager, gas) {
      requestDeployAuthorization(it)
    }
    dialog.show()

  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      onBackPressed()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun requestDeployAuthorization(gas: ContractGas) {
    val supply = deploy_contract__token_supply.editText?.value()?.toLong() ?: 0

    val dialog = ConfirmDeployDialog.create(supportFragmentManager, gas) { passphrase ->
      val contractInfo = ContractData(contractName, gas, latitude, longitude, supply)
      presenter.deployContract(passphrase, contractInfo)
    }
    dialog.show()
  }

  override fun showError(message: String?) {
    Snackbar.make(deploy_contract__deploy_fab, message ?: "error", Snackbar.LENGTH_LONG)
  }

  override fun complete(hex: String?) {
    showInfo(R.string.app_name, R.string.deploy_contract__contract_creation_submitted, hex.orEmpty()) {
      finish()
    }
  }

  private val contractName: String
    get() {
      val editText = deploy_contract__name.editText
      return editText?.text.toString()
    }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    super.onDestroy()
  }

  override fun setSuggestedGasPrice(gasPrice: Long) {

  }

  private fun updateMyMarker(latitude: Double, longitude: Double, map: GoogleMap) {
    val me = LatLng(latitude, longitude)

    val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp)

    val position = CameraPosition.builder()
        .target(me)
        .zoom(16f)
        .bearing(0.0f)
        .tilt(0.0f)
        .build()

    map.run {
      animateCamera(CameraUpdateFactory.newCameraPosition(position), null)
      moveCamera(CameraUpdateFactory.newCameraPosition(position))
      addMarker(MarkerOptions().position(me).title("Me"))
    }
  }

  override fun onMapReady(map: GoogleMap?) {
    val gMap = map ?: return
    updateMyMarker(latitude, longitude, gMap)
  }

  private val latitude: Double
    get() = intent.getDoubleExtra(LATITUDE, 0.0)

  private val longitude: Double
    get() = intent.getDoubleExtra(LONGITUDE, 0.0)

  companion object {
    private const val LONGITUDE = "io.sikorka.android.extras.LONGITUDE"
    private const val LATITUDE = "io.sikorka.android.extras.LATITUDE"

    fun start(context: Context, latitude: Double, longitude: Double) {
      val intent = Intent(context, DeployContractActivity::class.java)
      intent.putExtra(LATITUDE, latitude)
      intent.putExtra(LONGITUDE, longitude)
      context.startActivity(intent)
    }
  }
}