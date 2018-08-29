package io.sikorka.android.ui.accounts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.core.accounts.AccountsModel
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.MenuTint
import io.sikorka.android.ui.accounts.accountcreation.AccountCreationDialog
import io.sikorka.android.ui.accounts.accountexport.AccountExportActivity
import io.sikorka.android.ui.accounts.accountimport.AccountImportActivity
import io.sikorka.android.ui.dialogs.verifyPassphraseDialog
import kotterknife.bindView
import org.koin.android.ext.android.inject
import timber.log.Timber

class AccountActivity : BaseActivity(), AccountView {

  private val accountsRecycler: RecyclerView by bindView(R.id.accounts__recycler_view)

  private val createAccount: FloatingActionButton by bindView(R.id.accounts__create_account)

  private val presenter: AccountPresenter by inject()

  private val adapter: AccountAdapter by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__account)

    accountsRecycler.adapter = adapter
    accountsRecycler.layoutManager = LinearLayoutManager(this)
    setupToolbar(R.string.account_management__title)

    createAccount.setOnClickListener {
      val dialog = AccountCreationDialog.newInstance(supportFragmentManager) {
        presenter.loadAccounts()
      }
      dialog.show()
    }
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

  override fun onDestroy() {
    presenter.detach()

    super.onDestroy()
  }

  override fun accountsLoaded(accounts: AccountsModel) {
    Timber.v("Accounts ${accounts.accounts.size}")
    adapter.update(accounts)
  }

  override fun loading() {
  }

  override fun showError(message: String) {
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
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