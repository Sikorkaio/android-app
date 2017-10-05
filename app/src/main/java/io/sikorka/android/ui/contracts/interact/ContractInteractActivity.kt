package io.sikorka.android.ui.contracts.interact

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import kotlinx.android.synthetic.main.activity__contract_interact.*
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

    contract_interact__confirm_answer.setOnClickListener {
      presenter.confirmAnswer(answer)
    }

    contract_interact__contract_address.text = contractAddress
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
    val resId = if (confirmAnswer) R.string.contract_interact__answer_success else R.string.contract_interact__answer_wrong
    Snackbar.make(contract_interact__answer, resId, Snackbar.LENGTH_LONG).show()

  }

  override fun update(question: String, name: String) {
    supportActionBar?.title = name
    contract_interact__question_content.text = question
  }

  override fun showError() {
    Snackbar.make(contract_interact__answer, R.string.contract_interact__generic_error, Snackbar.LENGTH_LONG).show()
  }


  private val contractAddress: String
    get() = intent?.getStringExtra(CONTRACT_ADDRESS) ?: fail("expected a non null contract address")

  private val answer: String
    get() {
      val editText = contract_interact__answer.editText ?: fail("edittext was null")
      return editText.text.toString()
    }

  companion object {
    private const val CONTRACT_ADDRESS = "io.sikorka.android.extras.CONTRACT_ADDRESS"

    fun start(context: Context, contractAddress: String) {
      val intent = Intent(context, ContractInteractActivity::class.java)
      intent.putExtra(CONTRACT_ADDRESS, contractAddress)
      context.startActivity(intent)
    }
  }
}