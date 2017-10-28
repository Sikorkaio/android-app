package io.sikorka.android.node.contracts.data

/**
 * Created by Kelsos on 10/28/2017.
 */
interface IContractData {
  val name: String
  val gas: ContractGas
  val latitude: Double
  val longitude: Double
}