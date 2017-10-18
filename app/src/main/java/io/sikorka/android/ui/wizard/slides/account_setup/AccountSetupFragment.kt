package io.sikorka.android.ui.wizard.slides.account_setup


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.accounts.account_creation.AccountCreationDialog
import io.sikorka.android.ui.accounts.account_import.AccountImportActivity
import io.sikorka.android.ui.isVisible
import io.sikorka.android.ui.show
import toothpick.Toothpick
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [AccountSetupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountSetupFragment : Fragment(), AccountSetupView {

  @BindView(R.id.account_setup__account_address)
  internal lateinit var accountAddress: TextView

  @Inject internal lateinit var presenter: AccountSetupPresenter

  @OnClick(R.id.account_setup__create_new)
  internal fun onCreateNewPressed() {
    val dialog = AccountCreationDialog.newInstance(fragmentManager) {
      presenter.loadAccount()
    }
    dialog.show()
  }

  @OnClick(R.id.account_setup__import_account)
  internal fun onAccountImportPressed() {
    AccountImportActivity.start(context)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(context.applicationContext, this)
    scope.installModules(AccountSetupModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val layoutInflater = inflater ?: fail("no inflater?")
    val view = layoutInflater.inflate(R.layout.fragment__account_setup, container, false)
    ButterKnife.bind(this, view)
    return view
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
