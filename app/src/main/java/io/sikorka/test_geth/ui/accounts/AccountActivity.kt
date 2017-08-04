package io.sikorka.test_geth.ui.accounts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.test_geth.R
import org.ethereum.geth.Account
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject
import android.text.InputType
import butterknife.ButterKnife
import io.sikorka.test_geth.ui.accounts.creation_dialog.AccountCreationDialog


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
    ButterKnife.bind(this)
    adapter = AccountAdapter()
    accountsRecycler.adapter = adapter
    accountsRecycler.layoutManager = LinearLayoutManager(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.loadAccounts()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  @OnClick(R.id.accounts__create_account)
  internal fun onCreateAccountClicked() {
    val dialog = AccountCreationDialog()
    dialog.show()
  }

  override fun accountsLoaded(accounts: List<Account>) {
    adapter.update(accounts)
  }

  override fun loading() {

  }

  override fun showError(message: String) {

  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, AccountActivity::class.java)
      context.startActivity(intent)
    }
  }
}

