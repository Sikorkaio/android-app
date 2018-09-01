package io.sikorka.android.ui.contracts.pending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.sikorka.android.R
import io.sikorka.android.data.contracts.pending.PendingContract
import kotterknife.bindView

class PendingContractsAdapter :
  Adapter<PendingContractsAdapter.PendingContractsViewHolder>() {

  private var data: List<PendingContract> = emptyList()

  override fun onBindViewHolder(holder: PendingContractsViewHolder, position: Int) {
    holder.bindTo(data[position])
  }

  override fun getItemCount(): Int = data.size

  fun update(data: List<PendingContract>) {
    this.data = data
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingContractsViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.item__pending_contract, parent, false)
    return PendingContractsViewHolder(view)
  }

  class PendingContractsViewHolder(itemView: View) : ViewHolder(itemView) {

    private val address: TextView by bindView(R.id.pending_contract__contract_address)
    private val transaction: TextView by bindView(R.id.pending_contract__contract_transaction)

    fun bindTo(contract: PendingContract) {
      address.text = contract.contractAddress
      transaction.text = contract.transactionHash
    }
  }
}