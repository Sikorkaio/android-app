package io.sikorka.android.ui.wizard.slides.account_setup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.accounts.account_creation.AccountCreationDialog
import io.sikorka.android.ui.accounts.account_import.AccountImportActivity
import io.sikorka.android.ui.isVisible
import io.sikorka.android.ui.show
import kotterknife.bindView
import toothpick.Toothpick
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSetupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSetupFragment : Fragment(), AccountSetupView {

  private val accountAddress: TextView by bindView(R.id.account_setup__account_address)
  private val createNew: TextView by bindView(R.id.account_setup__create_new)
  private val importAccount: TextView by bindView(R.id.account_setup__import_account)

  @Inject
  lateinit var presenter: AccountSetupPresenter

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

  override fun onCreate(savedInstanceState: Bundle?) {
    val context = context ?: fail("")
    val scope = Toothpick.openScopes(context.applicationContext, this)
    scope.installModules(AccountSetupModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
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
      accountAddress.show()
    }
  }


  companion object {

    fun newInstance(): AccountSetupFragment {
      val fragment = AccountSetupFragment()
      return fragment
    }
  }

}
