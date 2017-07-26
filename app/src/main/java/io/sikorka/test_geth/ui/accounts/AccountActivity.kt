package io.sikorka.test_geth.ui.accounts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import io.sikorka.test_geth.R
import org.ethereum.geth.Account
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class AccountActivity : AppCompatActivity(), AccountView {
  @BindView(R.id.accounts__recycler_view) internal lateinit var accountsRecycler: RecyclerView

  @Inject internal lateinit var presenter: AccountPresenter

  private lateinit var adapter: AccountAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), AccountModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_account)
    presenter.loadAccounts()
    adapter = AccountAdapter()
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun accountsLoaded(accounts: List<Account>) {
    adapter.update(accounts)
  }

  override fun loading() {

  }

  override fun showError(message: String) {

  }
}

