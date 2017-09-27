package io.sikorka.android.ui.contracts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.sikorka.android.R
import io.sikorka.android.node.contracts.ContractData
import io.sikorka.android.node.contracts.ContractGas
import io.sikorka.android.ui.contracts.dialog.ConfirmDeployDialog
import io.sikorka.android.ui.dialogs.showInfo
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
      deploy_contract__answer.error = null
      deploy_contract__question.error = null

      if (question.isBlank()) {
        deploy_contract__question.error = getString(R.string.deploy_contract__question_empty)
        return@setOnClickListener
      }

      if (answer.isBlank()) {
        deploy_contract__answer.error = getString(R.string.deploy_contract__answer_empty)
        return@setOnClickListener
      }

      presenter.checkValues(gasPrice, gasLimit)
    }

  }

  override fun requestDeployAuthorization(gas: ContractGas) {
    val dialog = ConfirmDeployDialog.create(supportFragmentManager, gas) { passphrase ->
      val contractInfo = ContractData(gas, question, answer, latitude, longitude)
      presenter.deployContract(passphrase, contractInfo)
    }
    dialog.show()
  }

  override fun showError(message: String?) {
    Snackbar.make(deploy_contract__deploy_fab, message ?: "error", Snackbar.LENGTH_LONG)
  }

  override fun complete(hex: String?) {
    showInfo(R.string.app_name, R.string.deploy_contract_deployed_successfully, hex.orEmpty()) {
      finish()
    }
  }

  private val gasPrice: Double
    get() {
      val gasField = deploy_contract__gas_price.editText
      val value = gasField?.text.toString()
      return value.toDoubleOrNull() ?: 0.0
    }

  private val gasLimit: Double
    get() {
      val gasLimitField = deploy_contract__gas_limit.editText
      val value = gasLimitField?.text.toString()
      return value.toDoubleOrNull() ?: 0.0
    }

  private val question: String
    get() {
      val editText = deploy_contract__question.editText
      return editText?.text.toString()
    }

  private val answer: String
    get() {
      val editText = deploy_contract__answer.editText
      return editText?.text.toString()
    }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.load()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun setSuggestedGasPrice(gasPrice: Double) {
    val gasField = deploy_contract__gas_price.editText
    gasField?.setText(getString(R.string.deploy_contract__gas_price_value, gasPrice))
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

    map.animateCamera(CameraUpdateFactory.newCameraPosition(position), null)
    map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    map.addMarker(MarkerOptions().position(me).title("Me"))
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