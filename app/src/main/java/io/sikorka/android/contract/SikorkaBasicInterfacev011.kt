package io.sikorka.android.contract

// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!


// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!


import io.sikorka.android.helpers.hexStringToByteArray
import org.ethereum.geth.*

/**
 * @property contract contract instance bound to a blockchain address.
 */
class SikorkaBasicInterfacev011
private constructor(//
    private val contract: BoundContract
) {
  // Ethereum address where this contract is located at.
  val address: Address = contract.address

  // Ethereum transaction in which this contract was deployed (if known!).
  val Deployer: Transaction? = contract.deployer

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
    this.contract.call(opts, results, "detector", args)
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
    this.contract.call(opts, results, "name", args)
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
    this.contract.call(opts, results, "owner", args)
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
    this.contract.call(opts, results, "seconds_allowed", args)
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
    this.contract.call(opts, results, "version", args)
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


    return this.contract.transact(opts, "authorize_user", args)
  }

  // change_owner is a paid mutator transaction binding the contract method 0x253c8bd4.
  //
  // Solidity: function change_owner(_newOwner address) returns()
  @Throws(Exception::class)
  fun change_owner(opts: TransactOpts, _newOwner: Address): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = _newOwner


    return this.contract.transact(opts, "change_owner", args)
  }

  // simple_presence_check is a paid mutator transaction binding the contract method 0x15ace0a0.
  //
  // Solidity: function simple_presence_check(message bytes) returns()
  @Throws(Exception::class)
  fun simple_presence_check(opts: TransactOpts, message: ByteArray): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).binary = message
    return this.contract.transact(opts, "simple_presence_check", args)
  }

  companion object {
    // ABI is the input ABI used to generate the binding from.
    const val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"message","type":"bytes"}],"name":"simple_presence_check","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"check_proof_of_presence","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"seconds_allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"detector_signed_message","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"user","type":"address"},{"name":"duration","type":"uint256"}],"name":"authorize_user","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"detector_direct_authorization","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"detector","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_detector","type":"address"},{"name":"_latitude","type":"int256"},{"name":"_longitude","type":"int256"},{"name":"_seconds_allowed","type":"uint256"},{"name":"registry_address","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"}]"""


    // BYTECODE is the compiled bytecode used for deploying new contracts.
    val BYTECODE = "6060604052341561000f57600080fd5b60405161107c38038061107c833981016040528080518201919060200180519060200190919080519060200190919080519060200190919080519060200190919080519060200190919050506000336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555086600190805190602001906100b39291906101d7565b5084600281905550836003819055508260058190555085600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508190508073ffffffffffffffffffffffffffffffffffffffff16637ca8f9113087876040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018381526020018281526020019350505050600060405180830381600087803b15156101b757600080fd5b6102c65a03f115156101c857600080fd5b5050505050505050505061027c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061021857805160ff1916838001178555610246565b82800160010185558215610246579182015b8281111561024557825182559160200191906001019061022a565b5b5090506102539190610257565b5090565b61027991905b8082111561027557600081600090555060010161025d565b5090565b90565b610df18061028b6000396000f300606060405236156100ad576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde03146100b257806315ace0a01461014057806316b205511461019d578063253c8bd4146101fa57806354fd4d501461023357806378147881146102c15780638da5cb5b146102ea57806397ac98311461033f5780639f60eef61461039c578063b6ac0959146103de578063e4e20d63146103f3575b600080fd5b34156100bd57600080fd5b6100c5610448565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101055780820151818401526020810190506100ea565b50505050905090810190601f1680156101325780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561014b57600080fd5b61019b600480803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506104e6565b005b34156101a857600080fd5b6101f8600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610575565b005b341561020557600080fd5b610231600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061074e565b005b341561023e57600080fd5b6102466107ec565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561028657808201518184015260208101905061026b565b50505050905090810190601f1680156102b35780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156102cc57600080fd5b6102d4610825565b6040518082815260200191505060405180910390f35b34156102f557600080fd5b6102fd61082b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561034a57600080fd5b61039a600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610850565b005b34156103a757600080fd5b6103dc600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919080359060200190919050506108df565b005b34156103e957600080fd5b6103f16109c7565b005b34156103fe57600080fd5b610406610a6e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104de5780601f106104b3576101008083540402835291602001916104de565b820191906000526020600020905b8154815290600101906020018083116104c157829003601f168201915b505050505081565b6000806000806021850151935060418501519250600254841215156105115760025484039150610519565b836002540391505b6000821215151561052657fe5b6003548312151561053d5760035483039050610545565b826003540390505b6000811215151561055257fe5b6103e882138061056357506103e881135b151561056e57600080fd5b5050505050565b60008151141561058c576105876109c7565b61074b565b60017f0100000000000000000000000000000000000000000000000000000000000000028160008151811015156105bf57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614156106405761063b81610850565b61074a565b60027f01000000000000000000000000000000000000000000000000000000000000000281600181518110151561067357fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916141561073b576000600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561072d57600080fd5b610736816104e6565b610749565b6000151561074857600080fd5b5b5b5b50565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156107a957600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6040805190810160405280600581526020017f302e312e3100000000000000000000000000000000000000000000000000000081525081565b60055481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600061085e84610a94565b8093508194508295505050503373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415156108a457600080fd5b428167ffffffffffffffff161015156108bc57600080fd5b6005548167ffffffffffffffff164203111515156108d957600080fd5b50505050565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561093b57600080fd5b42600660008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555080600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505050565b6000600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905060008114151515610a1b57600080fd5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205481420311151515610a6b57600080fd5b50565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600080610aa2610db1565b610aaa610db1565b60008060008060008b519450604185039750610ac78c8987610c50565b9650610ad58c60018a610c50565b9550610ae087610d57565b935093509350856040518082805190602001908083835b602083101515610b1c5780518252602082019150602081019050602083039250610af7565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390209050600181838686604051600081526020016040526000604051602001526040518085600019166000191681526020018460ff1660ff16815260200183600019166000191681526020018260001916600019168152602001945050505050602060405160208103908084039060008661646e5a03f11515610bc957600080fd5b5050602060405103519a50600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168b73ffffffffffffffffffffffffffffffffffffffff16141515610c3057600080fd5b610c3986610d9b565b809a50819b50505050505050505050509193909250565b610c58610db1565b600082855110151515610c6a57600080fd5b60008410151515610c7a57600080fd5b838303604051805910610c8a5750595b908082528060200260200182016040525091508390505b82811015610d4f578481815181101515610cb757fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000282858303815181101515610d1257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050610ca1565b509392505050565b6000806000602084015192506040840151915060ff6041850151169050601b8160ff161480610d895750601c8160ff16145b1515610d9457600080fd5b9193909250565b60008060148301519150601c8301519050915091565b6020604051908101604052806000815250905600a165627a7a723058205c667702f891ec3b81430bdae533b74b35fb0c7643f0ed5d22b2001e6942d5c60029".hexStringToByteArray()

    // deploy deploys a new Ethereum contract, binding an instance of SikorkaBasicInterface to it.
    @Throws(Exception::class)
    fun deploy(auth: TransactOpts, client: EthereumClient, _name: String, _detector: Address, _latitude: BigInt, _longitude: BigInt, _seconds_allowed: BigInt, registry_address: Address): SikorkaBasicInterfacev011 {
      val args = Geth.newInterfaces(6)
      val nameInterface = Geth.newInterface()
      val detectorInterface = Geth.newInterface()
      val latitudeInterface = Geth.newInterface()
      val longituteInterface = Geth.newInterface()
      val secondsAllowedInterface = Geth.newInterface()
      val registryAddressInterface = Geth.newInterface()

      nameInterface.string = _name
      detectorInterface.address = _detector
      latitudeInterface.bigInt = _latitude
      longituteInterface.bigInt = _longitude
      secondsAllowedInterface.bigInt = _seconds_allowed
      registryAddressInterface.address = registry_address

      args.set(0, nameInterface)
      args.set(1, detectorInterface)
      args.set(2, latitudeInterface)
      args.set(3, longituteInterface)
      args.set(4, secondsAllowedInterface)
      args.set(5, registryAddressInterface)

      return SikorkaBasicInterfacev011(Geth.deployContract(auth, ABI, BYTECODE, client, args))
    }
  }

}
