package io.sikorka.android.ui.accounts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.android.R
import io.sikorka.android.node.accounts.AccountsModel
import io.sikorka.android.ui.MenuTint
import io.sikorka.android.ui.accounts.account_creation.AccountCreationDialog
import io.sikorka.android.ui.accounts.account_export.AccountExportActivity
import io.sikorka.android.ui.accounts.account_import.AccountImportActivity
import io.sikorka.android.ui.dialogs.verifyPassphraseDialog
import timber.log.Timber
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject


class AccountActivity : AppCompatActivity(), AccountView {
  @BindView(R.id.accounts__recycler_view) internal lateinit var accountsRecycler: RecyclerView

  @Inject lateinit var presenter: AccountPresenter
  @Inject lateinit var adapter: AccountAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), AccountModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account)
    ButterKnife.bind(this)
    accountsRecycler.adapter = adapter
    accountsRecycler.layoutManager = LinearLayoutManager(this)
    supportActionBar?.let {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
      it.setHomeButtonEnabled(true)
    }
  }


  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.loadAccounts()
    adapter.setAccountActionListeners({ account ->
      verifyPassphraseDialog { presenter.deleteAccount(account, it) }
    }, {
      AccountExportActivity.start(this, it.address.hex)
    }) {
      presenter.setDefault(it)
    }
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  @OnClick(R.id.accounts__create_account)
  internal fun onCreateAccountClicked(){
    val dialog = AccountCreationDialog.newInstance(supportFragmentManager) {
      presenter.loadAccounts()
    }
    dialog.show()
  }

  override fun accountsLoaded(accounts: AccountsModel) {
    Timber.v("Accounts ${accounts.accounts.size}")
    adapter.update(accounts)
  }

  override fun loading() {

  }

  override fun showError(message: String) {

  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      R.id.account_management__import_menu -> {
        AccountImportActivity.start(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.activity_accounts__menu, menu)
    menu?.let {
      MenuTint.on(it)
          .setMenuItemIconColor(ContextCompat.getColor(this, R.color.white))
          .apply(this)
    }

    return super.onCreateOptionsMenu(menu)
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, AccountActivity::class.java)
      context.startActivity(intent)
    }
  }
}

