package io.sikorka.android.ui.contracts.pending

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.sikorka.android.R
import io.sikorka.android.data.contracts.pending.PendingContract
import io.sikorka.android.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_pending_contracts.*
import org.koin.android.ext.android.inject

class PendingContractsActivity : BaseActivity(), PendingContractsView {

  private lateinit var pendingContractAdapter: PendingContractsAdapter

  private val presenter: PendingContractsPresenter by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pending_contracts)

    pendingContractAdapter = PendingContractsAdapter()
    pending_contracts__contract_list.apply {
      adapter = pendingContractAdapter
      layoutManager = LinearLayoutManager(this@PendingContractsActivity)
    }

    setupToolbar(R.string.pending_contracts__title)
    presenter.attach(this)
    presenter.load()
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }

  override fun update(data: List<PendingContract>) {
    pending_contract__no_data_group.isVisible = data.isEmpty()
    pendingContractAdapter.update(data)
  }

  override fun error(message: String?) {
    if (message == null) {
      return
    }
    Snackbar.make(pending_contracts__contract_list, message, Snackbar.LENGTH_SHORT).show()
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, PendingContractsActivity::class.java)
      context.startActivity(intent)
    }
  }
}