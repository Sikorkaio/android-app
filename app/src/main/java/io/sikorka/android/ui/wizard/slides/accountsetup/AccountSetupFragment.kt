package io.sikorka.android.ui.wizard.slides.accountsetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.accounts.accountcreation.AccountCreationDialog
import io.sikorka.android.ui.accounts.accountimport.AccountImportActivity
import kotterknife.bindView
import org.koin.android.ext.android.inject

class AccountSetupFragment : androidx.fragment.app.Fragment(), AccountSetupView {

  private val accountAddress: TextView by bindView(R.id.account_setup__account_address)
  private val createNew: TextView by bindView(R.id.account_setup__create_new)
  private val importAccount: TextView by bindView(R.id.account_setup__import_account)

  private val presenter: AccountSetupPresenter by inject()

  private fun onCreateNewPressed() {
    val fm = fragmentManager ?: fail("fragmentManager was null")
    val dialog = AccountCreationDialog.newInstance(fm) {
      presenter.loadAccount()
    }
    dialog.show()
  }

  private fun onAccountImportPressed() {
    val context = context ?: fail("context was null")
    AccountImportActivity.start(context)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment__account_setup, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    createNew.setOnClickListener { onCreateNewPressed() }
    importAccount.setOnClickListener { onAccountImportPressed() }
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.loadAccount()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun setAccount(accountHex: String) {
    accountAddress.text = accountHex
    if (!accountAddress.isVisible) {
      accountAddress.isVisible = true
    }
  }

  companion object {

    fun newInstance(): AccountSetupFragment {
      return AccountSetupFragment()
    }
  }
}