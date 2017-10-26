package io.sikorka.android.contract

// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!


// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!


import io.sikorka.android.helpers.hexStringToByteArray
import org.ethereum.geth.*

/**
 * @property Contract Contract instance bound to a blockchain address.
 */
class SikorkaBasicInterfacev011
private constructor(//
    private val Contract: BoundContract
) {
  // Ethereum address where this contract is located at.
  val Address: Address? = Contract.address

  // Ethereum transaction in which this contract was deployed (if known!).
  val Deployer: Transaction? = Contract.deployer

  // Creates a new instance of SikorkaBasicInterface, bound to a specific deployed contract.
  @Throws(Exception::class)
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(address, ABI, client))


  // detector is a free data retrieval call binding the contract method 0xe4e20d63.
  //
  // Solidity: function detector() constant returns(address)
  @Throws(Exception::class)
  fun detector(callOpts: CallOpts? = null): Address {
    var opts = callOpts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultAddress()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.Contract.call(opts, results, "detector", args)
    return results.get(0).address

  }


  // name is a free data retrieval call binding the contract method 0x06fdde03.
  //
  // Solidity: function name() constant returns(string)
  @Throws(Exception::class)
  fun name(callOpts: CallOpts? = null): String {
    var opts = callOpts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.Contract.call(opts, results, "name", args)
    return results.get(0).string

  }


  // owner is a free data retrieval call binding the contract method 0x8da5cb5b.
  //
  // Solidity: function owner() constant returns(address)
  @Throws(Exception::class)
  fun owner(callOpts: CallOpts? = null): Address {
    var opts = callOpts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultAddress()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.Contract.call(opts, results, "owner", args)
    return results.get(0).address

  }


  // seconds_allowed is a free data retrieval call binding the contract method 0x78147881.
  //
  // Solidity: function seconds_allowed() constant returns(uint256)
  @Throws(Exception::class)
  fun seconds_allowed(callOpts: CallOpts? = null): BigInt {
    var opts = callOpts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.Contract.call(opts, results, "seconds_allowed", args)
    return results.get(0).bigInt

  }


  // version is a free data retrieval call binding the contract method 0x54fd4d50.
  //
  // Solidity: function version() constant returns(string)
  @Throws(Exception::class)
  fun version(callOpts: CallOpts? = null): String {
    var opts = callOpts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.Contract.call(opts, results, "version", args)
    return results.get(0).string

  }


  // authorize_user is a paid mutator transaction binding the contract method 0x9f60eef6.
  //
  // Solidity: function authorize_user(user address, duration uint256) returns()
  @Throws(Exception::class)
  fun authorize_user(opts: TransactOpts, user: Address, duration: BigInt): Transaction {
    val args = Geth.newInterfaces(2)
    args.set(0, Geth.newInterface())
    args.get(0).address = user
    args.set(1, Geth.newInterface())
    args.get(1).bigInt = duration


    return this.Contract.transact(opts, "authorize_user", args)
  }

  // change_owner is a paid mutator transaction binding the contract method 0x253c8bd4.
  //
  // Solidity: function change_owner(_newOwner address) returns()
  @Throws(Exception::class)
  fun change_owner(opts: TransactOpts, _newOwner: Address): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = _newOwner


    return this.Contract.transact(opts, "change_owner", args)
  }

  // simple_presence_check is a paid mutator transaction binding the contract method 0x15ace0a0.
  //
  // Solidity: function simple_presence_check(message bytes) returns()
  @Throws(Exception::class)
  fun simple_presence_check(opts: TransactOpts, message: ByteArray): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).binary = message


    return this.Contract.transact(opts, "simple_presence_check", args)
  }

  companion object {
    // ABI is the input ABI used to generate the binding from.
    const val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"message","type":"bytes"}],"name":"simple_presence_check","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"check_proof_of_presence","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"seconds_allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"detector_signed_message","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"user","type":"address"},{"name":"duration","type":"uint256"}],"name":"authorize_user","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"detector_direct_authorization","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"detector","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_detector","type":"address"},{"name":"_latitude","type":"int256"},{"name":"_longitude","type":"int256"},{"name":"_seconds_allowed","type":"uint256"},{"name":"registry_address","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"}]"""


    // BYTECODE is the compiled bytecode used for deploying new contracts.
    val BYTECODE = "6060604052341561000f57600080fd5b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506101c58061005e6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063253c8bd4146100485780638da5cb5b1461008157600080fd5b341561005357600080fd5b61007f600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506100d6565b005b341561008c57600080fd5b610094610174565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561013157600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16815600a165627a7a72305820b7237d7e9f6ae1b82cdcad875411efcc8ba92b56536ba1bc44ea9e6fb86d58560029".hexStringToByteArray()

    // deploy deploys a new Ethereum contract, binding an instance of SikorkaBasicInterface to it.
    @Throws(Exception::class)
    fun deploy(auth: TransactOpts, client: EthereumClient, _name: String, _detector: Address, _latitude: BigInt, _longitude: BigInt, _seconds_allowed: BigInt, registry_address: Address): SikorkaBasicInterfacev011 {
      val args = Geth.newInterfaces(6)

      args.set(0, Geth.newInterface())
      args.get(0).string = _name

      args.set(1, Geth.newInterface())
      args.get(1).address = _detector

      args.set(2, Geth.newInterface())
      args.get(2).bigInt = _latitude

      args.set(3, Geth.newInterface())
      args.get(3).bigInt = _longitude

      args.set(4, Geth.newInterface())
      args.get(4).bigInt = _seconds_allowed

      args.set(5, Geth.newInterface())
      args.get(5).address = registry_address

      return SikorkaBasicInterfacev011(Geth.deployContract(auth, ABI, BYTECODE, client, args))
    }
  }

}
