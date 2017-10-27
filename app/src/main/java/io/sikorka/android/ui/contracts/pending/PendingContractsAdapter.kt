package io.sikorka.android.ui.contracts.pending

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.sikorka.android.R
import io.sikorka.android.data.PendingContract
import io.sikorka.android.helpers.fail


class PendingContractsAdapter : RecyclerView.Adapter<PendingContractsAdapter.PendingContractsViewHolder>() {

  private var data: List<PendingContract> = emptyList()

  override fun onBindViewHolder(holder: PendingContractsViewHolder?, position: Int) {
    holder?.apply {
      bindContract(data[adapterPosition])
    }
  }

  override fun getItemCount(): Int = data.size

  fun update(data: List<PendingContract>) {
    this.data = data
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PendingContractsViewHolder {
    val parentView = parent ?: fail("parent was not supposed to be null")
    val inflater = LayoutInflater.from(parentView.context)
    val view = inflater.inflate(R.layout.item__pending_contract, parentView, false)
    return PendingContractsViewHolder(view)
  }


  class PendingContractsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.pending_contract__contract_address)
    lateinit var address: TextView

    @BindView(R.id.pending_contract__contract_transaction)
    lateinit var transaction: TextView

    init {
      ButterKnife.bind(this, itemView)
    }

    fun bindContract(contract: PendingContract) {
      address.text = contract.contractAddress
      transaction.text = contract.transactionHash
    }
  }
}
