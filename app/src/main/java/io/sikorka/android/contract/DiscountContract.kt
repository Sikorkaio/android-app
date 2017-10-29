// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!

package io.sikorka.android.contract

import io.sikorka.android.helpers.hexStringToByteArray
import org.ethereum.geth.*

class DiscountContract
private constructor(private val contract: BoundContract) {


  // Ethereum address where this contract is located at.
  val address: Address = contract.address

  // Ethereum transaction in which this contract was deployed (if known!).
  val deployer: Transaction? = contract.deployer

  // Creates a new instance of DiscountContract, bound to a specific deployed contract.
  @Throws(Exception::class)
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(address, ABI, client))


  // allowance is a free data retrieval call binding the contract method 0xdd62ed3e.
  //
  // Solidity: function allowance(_owner address, _spender address) constant returns(uint256)
  @Throws(Exception::class)
  fun allowance(callOpts: CallOpts?, _owner: Address, _spender: Address): BigInt {
    var opts = callOpts
    val args = Geth.newInterfaces(2)
    args.set(0, Geth.newInterface())
    args.get(0).address = _owner
    args.set(1, Geth.newInterface())
    args.get(1).address = _spender


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.contract.call(opts, results, "allowance", args)
    return results.get(0).bigInt

  }


  // balanceOf is a free data retrieval call binding the contract method 0x70a08231.
  //
  // Solidity: function balanceOf(_owner address) constant returns(uint256)
  @Throws(Exception::class)
  fun balanceOf(callOpts: CallOpts?, _owner: Address): BigInt {
    var opts = callOpts
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = _owner


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.contract.call(opts, results, "balanceOf", args)
    return results.get(0).bigInt

  }


  // detector is a free data retrieval call binding the contract method 0xe4e20d63.
  //
  // Solidity: function detector() constant returns(address)
  @Throws(Exception::class)
  fun detector(opts: CallOpts?= null): Address {
    var opts = opts
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
  fun name(opts: CallOpts? = null): String {
    var opts = opts
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
  fun owner(opts: CallOpts?= null): Address {
    var opts = opts
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
  fun seconds_allowed(opts: CallOpts?= null): BigInt {
    var opts = opts
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


  // totalSupply is a free data retrieval call binding the contract method 0x18160ddd.
  //
  // Solidity: function totalSupply() constant returns(uint256)
  @Throws(Exception::class)
  fun totalSupply(opts: CallOpts?= null): BigInt {
    var opts = opts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)


    if (opts == null) {
      opts = Geth.newCallOpts()
    }
    this.contract.call(opts, results, "totalSupply", args)
    return results.get(0).bigInt

  }


  // version is a free data retrieval call binding the contract method 0x54fd4d50.
  //
  // Solidity: function version() constant returns(string)
  @Throws(Exception::class)
  fun version(opts: CallOpts?= null): String {
    var opts = opts
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


  // approve is a paid mutator transaction binding the contract method 0x095ea7b3.
  //
  // Solidity: function approve(_spender address, _value uint256) returns(bool)
  @Throws(Exception::class)
  fun approve(opts: TransactOpts, _spender: Address, _value: BigInt): Transaction {
    val args = Geth.newInterfaces(2)
    args.set(0, Geth.newInterface())
    args.get(0).address = _spender
    args.set(1, Geth.newInterface())
    args.get(1).bigInt = _value


    return this.contract.transact(opts, "approve", args)
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

  // claimToken is a paid mutator transaction binding the contract method 0x7d3fd188.
  //
  // Solidity: function claimToken(data bytes) returns()
  @Throws(Exception::class)
  fun claimToken(opts: TransactOpts, data: ByteArray): Transaction {
    val args = Geth.newInterfaces(1)
    val dataInterface = Geth.newInterface()
    dataInterface.binary = data
    args.set(0, dataInterface)
    return this.contract.transact(opts, "claimToken", args)
  }

  // transfer is a paid mutator transaction binding the contract method 0xbe45fd62.
  //
  // Solidity: function transfer(_to address, _value uint256, _data bytes) returns(bool)
  @Throws(Exception::class)
  fun transfer(opts: TransactOpts, _to: Address, _value: BigInt, _data: ByteArray): Transaction {
    val args = Geth.newInterfaces(3)
    args.set(0, Geth.newInterface())
    args.get(0).address = _to
    args.set(1, Geth.newInterface())
    args.get(1).bigInt = _value
    args.set(2, Geth.newInterface())
    args.get(2).binary = _data


    return this.contract.transact(opts, "transfer", args)
  }

  // transferFrom is a paid mutator transaction binding the contract method 0x23b872dd.
  //
  // Solidity: function transferFrom(_from address, _to address, _value uint256) returns(bool)
  @Throws(Exception::class)
  fun transferFrom(opts: TransactOpts, _from: Address, _to: Address, _value: BigInt): Transaction {
    val args = Geth.newInterfaces(3)
    args.set(0, Geth.newInterface())
    args.get(0).address = _from
    args.set(1, Geth.newInterface())
    args.get(1).address = _to
    args.set(2, Geth.newInterface())
    args.get(2).bigInt = _value


    return this.contract.transact(opts, "transferFrom", args)
  }

  companion object {
    // ABI is the input ABI used to generate the binding from.
    val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_value","type":"uint256"}],"name":"approve","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"simple_presence_check","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"check_proof_of_presence","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transferFrom","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"seconds_allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"data","type":"bytes"}],"name":"claimToken","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"detector_signed_message","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"user","type":"address"},{"name":"duration","type":"uint256"}],"name":"authorize_user","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"detector_direct_authorization","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"_data","type":"bytes"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_spender","type":"address"}],"name":"allowance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"detector","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_detector","type":"address"},{"name":"_latitude","type":"int256"},{"name":"_longitude","type":"int256"},{"name":"_seconds_allowed","type":"uint256"},{"name":"_registry_address","type":"address"},{"name":"_totalSupply","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_owner","type":"address"},{"indexed":true,"name":"_spender","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Approval","type":"event"}]"""


    // BYTECODE is the compiled bytecode used for deploying new contracts.
    val BYTECODE = "606060405234156200001057600080fd5b604051620012003803806200120083398101604052808051820191906020018051919060200180519190602001805191906020018051919060200180519190602001805160008054600160a060020a03191633600160a060020a03161781559092508891508790879087908790879060018780516200009492916020019062000180565b505060028490556003839055600582905560048054600160a060020a031916600160a060020a038781169190911790915581908116637ca8f9113087876040517c010000000000000000000000000000000000000000000000000000000063ffffffff8616028152600160a060020a03909316600484015260248301919091526044820152606401600060405180830381600087803b15156200013657600080fd5b6102c65a03f115156200014857600080fd5b5050506008889055505050600160a060020a033016600090815260096020526040902094909455506200022598505050505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001c357805160ff1916838001178555620001f3565b82800160010185558215620001f3579182015b82811115620001f3578251825591602001919060010190620001d6565b506200020192915062000205565b5090565b6200022291905b808211156200020157600081556001016200020c565b90565b610fcb80620002356000396000f3006060604052600436106101065763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461010b578063095ea7b31461019557806315ace0a0146101cb57806316b205511461021e57806318160ddd1461026f57806323b872dd14610294578063253c8bd4146102bc57806354fd4d50146102db57806370a08231146102ee578063781478811461030d5780637d3fd188146103205780638da5cb5b1461037157806397ac9831146103a05780639f60eef6146103f1578063a9059cbb14610413578063b6ac095914610435578063be45fd6214610448578063dd62ed3e146104ad578063e4e20d63146104d2575b600080fd5b341561011657600080fd5b61011e6104e5565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561015a578082015183820152602001610142565b50505050905090810190601f1680156101875780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101a057600080fd5b6101b7600160a060020a0360043516602435610583565b604051901515815260200160405180910390f35b34156101d657600080fd5b61021c60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061063e95505050505050565b005b341561022957600080fd5b61021c60046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506106c595505050505050565b341561027a57600080fd5b610282610799565b60405190815260200160405180910390f35b341561029f57600080fd5b6101b7600160a060020a036004358116906024351660443561079f565b34156102c757600080fd5b61021c600160a060020a03600435166108fb565b34156102e657600080fd5b61011e610945565b34156102f957600080fd5b610282600160a060020a036004351661097c565b341561031857600080fd5b610282610997565b341561032b57600080fd5b61021c60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061099d95505050505050565b341561037c57600080fd5b6103846109b7565b604051600160a060020a03909116815260200160405180910390f35b34156103ab57600080fd5b61021c60046024813581810190830135806020601f820181900481020160405190810160405281815292919060208401838380828437509496506109c695505050505050565b34156103fc57600080fd5b61021c600160a060020a0360043516602435610a2b565b341561041e57600080fd5b6101b7600160a060020a0360043516602435610a6f565b341561044057600080fd5b61021c610b62565b341561045357600080fd5b6101b760048035600160a060020a03169060248035919060649060443590810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610baf95505050505050565b34156104b857600080fd5b610282600160a060020a0360043581169060243516610cde565b34156104dd57600080fd5b610384610d09565b60018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561057b5780601f106105505761010080835404028352916020019161057b565b820191906000526020600020905b81548152906001019060200180831161055e57829003601f168201915b505050505081565b6000600160a060020a038316151561059a57600080fd5b8115806105ca5750600160a060020a033381166000908152600a6020908152604080832093871683529290522054155b15156105d557600080fd5b600160a060020a033381166000818152600a6020908152604080832094881680845294909152908190208590557f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a350600192915050565b60008060008060218501519350604185015160025490935084126106685760025484039150610670565b836002540391505b600082121561067b57fe5b600354831261068f57506003548203610697565b826003540390505b60008112156106a257fe5b6103e88213806106b357506103e881135b15156106be57600080fd5b5050505050565b805115156106da576106d5610b62565b610796565b806000815181106106e757fe5b01602001517fff0000000000000000000000000000000000000000000000000000000000000060f860020a918290048202161415610728576106d5816109c6565b8060018151811061073557fe5b016020015160f860020a900460f860020a027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916600260f860020a02141561010657600454600160a060020a03161561078d57600080fd5b6106d58161063e565b50565b60085481565b6000600160a060020a03841615156107b657600080fd5b600160a060020a03831615156107cb57600080fd5b30600160a060020a031683600160a060020a0316141515156107ec57600080fd5b600160a060020a0384166000908152600960205260409020548290101561081257600080fd5b600160a060020a038085166000908152600a6020908152604080832033909416835292905220548290101561084657600080fd5b600160a060020a038316600090815260096020526040902054828101101561086d57600080fd5b600160a060020a03808416600081815260096020908152604080832080548801905588851680845281842080548990039055600a83528184203390961684529490915290819020805486900390559091907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a35060019392505050565b60005433600160a060020a0390811691161461091657600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b60408051908101604052600581527f302e312e31000000000000000000000000000000000000000000000000000000602082015281565b600160a060020a031660009081526009602052604090205490565b60055481565b806109a7816106c5565b6109b2336064610a6f565b505050565b600054600160a060020a031681565b60008060006109d484610d18565b9194509250905033600160a060020a03908116908316146109f457600080fd5b4267ffffffffffffffff821610610a0a57600080fd5b60055467ffffffffffffffff821642031115610a2557600080fd5b50505050565b60045433600160a060020a03908116911614610a4657600080fd5b600160a060020a0390911660009081526006602090815260408083204290556007909152902055565b6000600160a060020a0383161515610a8657600080fd5b30600160a060020a031683600160a060020a031614151515610aa757600080fd5b600160a060020a03331660009081526009602052604090205482901015610acd57600080fd5b600160a060020a0383166000908152600960205260409020548281011015610af457600080fd5b600160a060020a033381166000818152600960205260408082208054879003905592861680825290839020805486019055917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a350600192915050565b600160a060020a033316600090815260066020526040902054801515610b8757600080fd5b600160a060020a03331660009081526007602052604090205442829003111561079657600080fd5b6000806000610bbe8686610a6f565b1515610bc957600080fd5b853b91506000821115610cd2575084600160a060020a03811663c0ee0b8a3387876040518463ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018084600160a060020a0316600160a060020a0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610c70578082015183820152602001610c58565b50505050905090810190601f168015610c9d5780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b1515610cbd57600080fd5b6102c65a03f11515610cce57600080fd5b5050505b50600195945050505050565b600160a060020a039182166000908152600a6020908152604080832093909416825291909152205490565b600454600160a060020a031681565b600080600080610d26610f8d565b610d2e610f8d565b60008060008060008b519450604185039750610d4b8c8987610e6e565b9650610d598c60018a610e6e565b9550610d6487610f33565b935093509350856040518082805190602001908083835b60208310610d9a5780518252601f199092019160209182019101610d7b565b6001836020036101000a0380198251168184511617909252505050919091019250604091505051809103902090506001818386866040516000815260200160405260006040516020015260405193845260ff90921660208085019190915260408085019290925260608401929092526080909201915160208103908084039060008661646e5a03f11515610e2d57600080fd5b505060206040510351600454909b50600160a060020a03808d16911614610e5357600080fd5b610e5c86610f77565b9b9d909c509950505050505050505050565b610e76610f8d565b60008285511015610e8657600080fd5b6000841015610e9457600080fd5b838303604051805910610ea45750595b818152601f19601f8301168101602001604052905091508390505b82811015610f2b57848181518110610ed357fe5b016020015160f860020a900460f860020a028285830381518110610ef357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600101610ebf565b509392505050565b6000806000602084015192506040840151915060ff60418501511690508060ff16601b1480610f6557508060ff16601c145b1515610f7057600080fd5b9193909250565b60008060148301519150601c8301519050915091565b602060405190810160405260008152905600a165627a7a72305820fb183615a96c804b72f28c7af493b3adc17738b7d908d03b4ebf108aad94c3be0029".hexStringToByteArray()

    // deploy deploys a new Ethereum contract, binding an instance of DiscountContract to it.
    @Throws(Exception::class)
    fun deploy(
        auth: TransactOpts,
        client: EthereumClient,
        _name: String,
        _detector: Address,
        _latitude: BigInt,
        _longitude: BigInt,
        _seconds_allowed: BigInt,
        _registry_address: Address,
        _totalSupply: BigInt
    ): DiscountContract {
      val args = Geth.newInterfaces(7)

      val nameInterface = Geth.newInterface()
      val detectorInterface = Geth.newInterface()
      val latitudeInterface = Geth.newInterface()
      val longitudeInterface = Geth.newInterface()
      val secondsAllowedInterface = Geth.newInterface()
      val registryAddressInterface = Geth.newInterface()
      val totalSupply = Geth.newInterface()

      nameInterface.string = _name
      detectorInterface.address = _detector
      latitudeInterface.bigInt = _latitude
      longitudeInterface.bigInt = _longitude
      secondsAllowedInterface.bigInt = _seconds_allowed
      registryAddressInterface.address = _registry_address
      totalSupply.bigInt = _totalSupply

      args.set(0, nameInterface)
      args.set(1, detectorInterface)
      args.set(2, latitudeInterface)
      args.set(3, longitudeInterface)
      args.set(4, secondsAllowedInterface)
      args.set(5, registryAddressInterface)
      args.set(6, totalSupply)


      return DiscountContract(Geth.deployContract(auth, ABI, BYTECODE, client, args))
    }
  }

}



