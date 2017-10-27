package io.sikorka.android.contract

import org.ethereum.geth.*


/**
 * The wrapper of the SikorkaRegistry Contract
 *
 * @property contract instance bound to a blockchain address.
 */
class SikorkaRegistry
private constructor(private val contract: BoundContract) {

  /**
   * Ethereum address where this contract is located at.
   */
  val Address: Address? = contract.address

  /**
   *Ethereum transaction in which this contract was deployed (if known!).
   */
  val Deployer: Transaction? = contract.deployer

  /**
   * Creates a new instance of SikorkaRegistry, bound to a specific deployed contract.
   */
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(address, ABI, client))


  // contractExists is a free data retrieval call binding the contract method 0x7709bc78.
  //
  // Solidity: function contractExists(channel address) constant returns(bool)
  fun contractExists(opts: CallOpts?, channel: Address): Boolean {
    var callOpts = opts
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = channel


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBool()
    results.set(0, result0)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "contractExists", args)
    return results.get(0).bool

  }


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
   * SikorkaContractResults is the output of a call to sikorka_contracts.
   */
  inner class SikorkaContractResults {
    var Contract_address: Address? = null
    var Latitude: BigInt? = null
    var Longitude: BigInt? = null

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
   * sikorka_contracts is a free data retrieval call binding the contract method 0xf7d2b101.
   *
   * ```Solidity: function sikorka_contracts( uint256) constant returns(contract_address address, latitude uint256, longitude uint256)```
   */
  fun sikorkaContracts(opts: CallOpts?, arg0: BigInt): SikorkaContractResults {
    var callOpts = opts
    val args = Geth.newInterfaces(1)
    val newInterface = Geth.newInterface()
    newInterface.bigInt = arg0
    args.set(0, newInterface)


    val results = Geth.newInterfaces(3)
    val result0 = Geth.newInterface()
    result0.setDefaultAddress()
    results.set(0, result0)
    val result1 = Geth.newInterface()
    result1.setDefaultBigInt()
    results.set(1, result1)
    val result2 = Geth.newInterface()
    result2.setDefaultBigInt()
    results.set(2, result2)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "sikorka_contracts", args)

    val result = SikorkaContractResults()
    result.Contract_address = results.get(0).address
    result.Latitude = results.get(1).bigInt
    result.Longitude = results.get(2).bigInt

    return result

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
     * The address of the registry contract that we are going to inteface with.
     */
    const val REGISTRY_ADDRESS = "0x6874D180D4D2e706874b0E3e078d26E43bed54dA"

    /**
     * Binds the Sikorka Registry to provide access to the deployed contracts.
     */
    fun bind(ethereumClient: EthereumClient): SikorkaRegistry {
      return SikorkaRegistry(Geth.newAddressFromHex(REGISTRY_ADDRESS), ethereumClient)
    }

  }

}


