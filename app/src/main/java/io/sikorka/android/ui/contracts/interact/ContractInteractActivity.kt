package io.sikorka.android.ui.contracts.interact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.gone
import kotlinx.android.synthetic.main.activity__contract_interact.*
import me.dm7.barcodescanner.zxing.sample.QrScannerActivity
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class ContractInteractActivity : AppCompatActivity(), ContractInteractView {

  @Inject
  lateinit var presenter: ContractInteractPresenter

  private lateinit var scope: Scope

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), ContractInteractModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__contract_interact)
    Toothpick.inject(this, scope)

    supportActionBar?.apply {
      setHomeButtonEnabled(true)
      setDisplayHomeAsUpEnabled(true)
      title = ""
    }

    contract_interact__verify.setOnClickListener {
      presenter.verify("")
      QrScannerActivity.start(this)
    }

    contract_interact__contract_address.text = contractAddress
  }

  override fun detector(hex: String) {
    interact_contract__detector_address.text = hex
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.load(contractAddress)
  }

  override fun noDetector() {
    interact_contract__detector_address_group.gone()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) {
      onBackPressed()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showConfirmationResult(confirmAnswer: Boolean) {

  }

  override fun update(name: String) {
    supportActionBar?.title = name
  }

  override fun showError() {
    Snackbar.make(contract_interact__verify, R.string.contract_interact__generic_error, Snackbar.LENGTH_LONG).show()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Timber.v("result $resultCode")
    if (requestCode == QrScannerActivity.SCANNER_RESULT && resultCode == Activity.RESULT_OK) {
      Timber.v(data?.getStringExtra(QrScannerActivity.DATA))
    }
    super.onActivityResult(requestCode, resultCode, data)
  }


  private val contractAddress: String
    get() = intent?.getStringExtra(CONTRACT_ADDRESS) ?: fail("expected a non null contract address")


  companion object {
    private const val CONTRACT_ADDRESS = "io.sikorka.android.extras.CONTRACT_ADDRESS"

    fun start(context: Context, contractAddress: String) {
      val intent = Intent(context, ContractInteractActivity::class.java)
      intent.putExtra(CONTRACT_ADDRESS, contractAddress)
      context.startActivity(intent)
    }
  }
}