package io.sikorka.android.contract

import org.ethereum.geth.*


/**
 * The wrapper of the SikorkaRegistry Contract
 *
 * @property contract instance bound to a blockchain address.
 */
class SikorkaRegistry
constructor(private val contract: BoundContract) {

  /**
   * Ethereum address where this contract is located at.
   */
  val address: Address? = contract.address

  /**
   * Creates a new instance of SikorkaRegistry, bound to a specific deployed contract.
   */
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(address, ABI, client))


  /**
   * getContractAddresses is a free data retrieval call binding the contract method 0x953874d2.
   *
   * ```Solidity: function getContractAddresses() constant returns(address[])```
   */
  fun getContractAddresses(opts: CallOpts? = null): Addresses {
    var callOpts = opts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultAddresses()
    results.set(0, result0)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "getContractAddresses", args)
    return results.get(0).addresses

  }


  /**
   * getContractCoordinates is a free data retrieval call binding the contract method 0xde866a70.
   *
   * ```Solidity: function getContractCoordinates() constant returns(uint256[])```
   */
  fun getContractCoordinates(opts: CallOpts? = null): BigInts {
    var callOpts = opts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInts()
    results.set(0, result0)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "getContractCoordinates", args)
    return results.get(0).bigInts

  }


  /**
   * removeContract is a paid mutator transaction binding the contract method 0xc375c2ef.
   *
   * ```Solidity: function removeContract(contract_address address) returns()```
   */
  fun removeContract(opts: TransactOpts, contract_address: Address): Transaction {
    val args = Geth.newInterfaces(1)
    val addressInterface = Geth.newInterface()
    addressInterface.address = contract_address
    args.set(0, addressInterface)
    return this.contract.transact(opts, "removeContract", args)
  }

  companion object {
    /**
     * ABI is the input ABI used to generate the binding from.
     */
    const val ABI = """[{"constant":true,"inputs":[{"name":"channel","type":"address"}],"name":"contractExists","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"contract_address","type":"address"},{"name":"latitude","type":"int256"},{"name":"longitude","type":"int256"}],"name":"addContract","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getContractAddresses","outputs":[{"name":"","type":"address[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"contract_address","type":"address"}],"name":"removeContract","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getContractCoordinates","outputs":[{"name":"","type":"int256[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"uint256"}],"name":"sikorka_contracts","outputs":[{"name":"contract_address","type":"address"},{"name":"latitude","type":"int256"},{"name":"longitude","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"anonymous":false,"inputs":[{"indexed":false,"name":"contract_address","type":"address"},{"indexed":false,"name":"latitude","type":"int256"},{"indexed":false,"name":"longitude","type":"int256"}],"name":"ContractAdded","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"contract_address","type":"address"}],"name":"ContractRemoved","type":"event"}]"""

    /**
     * The address of the registry contract that we are going to interface with.
     */
    const val REGISTRY_ADDRESS = "0xB1750e5375330F158a9E8EB82DFC470389175613"

    /**
     * Binds the Sikorka Registry to provide access to the deployed contracts.
     */
    fun bind(ethereumClient: EthereumClient): SikorkaRegistry {
      return SikorkaRegistry(Geth.newAddressFromHex(REGISTRY_ADDRESS), ethereumClient)
    }

  }

}


