package io.sikorka.android.ui.contracts.pending

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.data.contracts.pending.PendingContract
import io.sikorka.android.ui.gone
import io.sikorka.android.ui.show
import kotlinx.android.synthetic.main.activity_pending_contracts.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject

class PendingContractsActivity : AppCompatActivity(), PendingContractsView {

  private lateinit var scope: Scope
  private lateinit var pendingContractAdapter: PendingContractsAdapter

  @Inject
  lateinit var presenter: PendingContractsPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(PendingContractsModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieSupportActivityModule(this))
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pending_contracts)

    Toothpick.inject(this, scope)
    pendingContractAdapter = PendingContractsAdapter()
    pending_contracts__contract_list.apply {
      adapter = pendingContractAdapter
      layoutManager = LinearLayoutManager(this@PendingContractsActivity)
    }

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
      title = getString(R.string.pending_contracts__title)
    }
    presenter.attach(this)
    presenter.load()
  }

  override fun onDestroy() {
    presenter.detach()
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    super.onDestroy()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun update(data: List<PendingContract>) {
    if (data.isEmpty()) {
      pending_contract__no_data_group.show()
    } else {
      pending_contract__no_data_group.gone()
    }
    pendingContractAdapter.update(data)
  }

  override fun error(message: String?) {
    if (message == null) {
      return
    }
    Snackbar.make(pending_contracts__contract_list, message, Snackbar.LENGTH_SHORT).show()
  }


  @javax.inject.Scope
  @Target(AnnotationTarget.CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java
    fun start(context: Context) {
      val intent = Intent(context, PendingContractsActivity::class.java)
      context.startActivity(intent)
    }
  }
}
