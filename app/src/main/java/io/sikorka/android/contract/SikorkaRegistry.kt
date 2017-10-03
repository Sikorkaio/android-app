package io.sikorka.android.contract

import io.sikorka.android.helpers.hexStringToByteArray
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
   * addContract is a paid mutator transaction binding the contract method 0x8f33221e.
   *
   * ```Solidity: function addContract(contract_address address, latitude uint256, longitude uint256) returns()```
   */
  fun addContract(opts: TransactOpts, contract_address: Address, latitude: BigInt, longitude: BigInt): Transaction {
    val args = Geth.newInterfaces(3)
    args.set(0, Geth.newInterface())
    args.get(0).address = contract_address
    args.set(1, Geth.newInterface())
    args.get(1).bigInt = latitude
    args.set(2, Geth.newInterface())
    args.get(2).bigInt = longitude


    return this.contract.transact(opts, "addContract", args)
  }

  /**
   * removeContract is a paid mutator transaction binding the contract method 0xc375c2ef.
   *
   * ```Solidity: function removeContract(contract_address address) returns()```
   */
  fun removeContract(opts: TransactOpts, contract_address: Address): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = contract_address
    return this.contract.transact(opts, "removeContract", args)
  }

  companion object {
    /**
     * ABI is the input ABI used to generate the binding from.
     */
    const val ABI = """[{"constant":true,"inputs":[{"name":"channel","type":"address"}],"name":"contractExists","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"contract_address","type":"address"},{"name":"latitude","type":"uint256"},{"name":"longitude","type":"uint256"}],"name":"addContract","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getContractAddresses","outputs":[{"name":"","type":"address[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"contract_address","type":"address"}],"name":"removeContract","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"getContractCoordinates","outputs":[{"name":"","type":"uint256[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"uint256"}],"name":"sikorka_contracts","outputs":[{"name":"contract_address","type":"address"},{"name":"latitude","type":"uint256"},{"name":"longitude","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"anonymous":false,"inputs":[{"indexed":false,"name":"contract_address","type":"address"},{"indexed":false,"name":"latitude","type":"uint256"},{"indexed":false,"name":"longitude","type":"uint256"}],"name":"ContractAdded","type":"event"},{"anonymous":false,"inputs":[{"indexed":false,"name":"contract_address","type":"address"}],"name":"ContractRemoved","type":"event"}]"""

    /**
     * BYTECODE is the compiled bytecode used for deploying new contracts
     */
    val BYTECODE = "6060604052341561000f57600080fd5b6108948061001e6000396000f30060606040523615610076576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680637709bc781461007b5780638f33221e146100cc578063953874d214610117578063c375c2ef14610181578063de866a70146101ba578063f7d2b10114610224575b600080fd5b341561008657600080fd5b6100b2600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610295565b604051808215151515815260200191505060405180910390f35b34156100d757600080fd5b610115600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919080359060200190919080359060200190919050506102a8565b005b341561012257600080fd5b61012a61043c565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561016d578082015181840152602081019050610152565b505050509050019250505060405180910390f35b341561018c57600080fd5b6101b8600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610526565b005b34156101c557600080fd5b6101cd610680565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156102105780820151818401526020810190506101f5565b505050509050019250505060405180910390f35b341561022f57600080fd5b610245600480803590602001909190505061075f565b604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001935050505060405180910390f35b600080823b905060008111915050919050565b60006102b384610295565b15156102be57600080fd5b600080548060010182816102d291906107b8565b916000526020600020906003020160006060604051908101604052808873ffffffffffffffffffffffffffffffffffffffff16815260200187815260200186815250909190915060008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506020820151816001015560408201518160020155505050600080549050905080600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055507f416a85ad46a79c0c8942d936d910a83796d3d0d516990461d23b9f19073715ca848484604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001935050505060405180910390a150505050565b6104446107ea565b600061044e6107ea565b6000805490506040518059106104615750595b90808252806020026020018201604052509050600091505b60008054905082101561051e5760008281548110151561049557fe5b906000526020600020906003020160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681838151811015156104d557fe5b9060200190602002019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508180600101925050610479565b809250505090565b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000811415151561057a57600080fd5b60006001820381548110151561058c57fe5b9060005260206000209060030201600080820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556001820160009055600282016000905550506000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055507f8d30d41865a0b811b9545d879520d2dde9f4cc49e4241f486ad9752bc904b56582604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a15050565b6106886107fe565b60006106926107fe565b6002600080549050026040518059106106a85750595b90808252806020026020018201604052509050600091505b600080549050821015610757576000828154811015156106dc57fe5b90600052602060002090600302016001015481838151811015156106fc57fe5b906020019060200201818152505060008281548110151561071957fe5b906000526020600020906003020160020154816001840181518110151561073c57fe5b906020019060200201818152505081806001019250506106c0565b809250505090565b60008181548110151561076e57fe5b90600052602060002090600302016000915090508060000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010154908060020154905083565b8154818355818115116107e5576003028160030283600052602060002091820191016107e49190610812565b5b505050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b61086591905b8082111561086157600080820160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690556001820160009055600282016000905550600301610818565b5090565b905600a165627a7a72305820f1bce23b17f97ad3b962aeaf501f642f960bed27c0de081d0197534489a294d40029".hexStringToByteArray()

    /**
     * The address of the registry contract that we are going to inteface with.
     */
    const val REGISTRY_ADDRESS = "0xC0e1Eedee8eAB99966a11c2c10cB6aAe4846CDA7"

    /**
     * Binds the Sikorka Registry to provide access to the deployed contracts.
     */
    fun bind(ethereumClient: EthereumClient): SikorkaRegistry {
      return SikorkaRegistry(Geth.newAddressFromHex(REGISTRY_ADDRESS), ethereumClient)
    }

  }

}


