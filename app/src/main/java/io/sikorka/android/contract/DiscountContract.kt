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
    return this.contract.transact(opts, "claimTokens", args)
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
    val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_value","type":"uint256"}],"name":"approve","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"simple_presence_check","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"check_proof_of_presence","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transferFrom","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"seconds_allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"signed_timestamp_check","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"detector_signed_message","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"user","type":"address"},{"name":"duration","type":"uint256"}],"name":"authorize_user","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"detector_direct_authorization","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"_data","type":"bytes"}],"name":"transfer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"data","type":"bytes"}],"name":"claimTokens","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_spender","type":"address"}],"name":"allowance","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"detector","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_detector","type":"address"},{"name":"_latitude","type":"int256"},{"name":"_longitude","type":"int256"},{"name":"_seconds_allowed","type":"uint256"},{"name":"_registry_address","type":"address"},{"name":"_totalSupply","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_owner","type":"address"},{"indexed":true,"name":"_spender","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Approval","type":"event"}]"""


    // BYTECODE is the compiled bytecode used for deploying new contracts.
    val BYTECODE = "606060405234156200001057600080fd5b60405162001f3b38038062001f3b833981016040528080518201919060200180519060200190919080519060200190919080519060200190919080519060200190919080519060200190919080519060200190919050508686868686866000336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508660019080519060200190620000c792919062000240565b5084600281905550836003819055508260058190555085600460006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508190508073ffffffffffffffffffffffffffffffffffffffff16637ca8f9113087876040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018381526020018281526020019350505050600060405180830381600087803b1515620001cc57600080fd5b6102c65a03f11515620001de57600080fd5b505050505050505050508060088190555080600960003073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555050505050505050620002ef565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200028357805160ff1916838001178555620002b4565b82800160010185558215620002b4579182015b82811115620002b357825182559160200191906001019062000296565b5b509050620002c39190620002c7565b5090565b620002ec91905b80821115620002e8576000816000905550600101620002ce565b5090565b90565b611c3c80620002ff6000396000f300606060405260043610610112576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde0314610117578063095ea7b3146101a557806315ace0a0146101ff57806316b205511461025c57806318160ddd146102b957806323b872dd146102e2578063253c8bd41461035b57806354fd4d501461039457806370a0823114610422578063781478811461046f5780638da5cb5b146104985780638f3e9d79146104ed57806397ac98311461054a5780639f60eef6146105a7578063a9059cbb146105e9578063b6ac095914610643578063be45fd6214610658578063dac90e64146106f5578063dd62ed3e14610752578063e4e20d63146107be575b600080fd5b341561012257600080fd5b61012a610813565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561016a57808201518184015260208101905061014f565b50505050905090810190601f1680156101975780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101b057600080fd5b6101e5600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919080359060200190919050506108b1565b604051808215151515815260200191505060405180910390f35b341561020a57600080fd5b61025a600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610a5e565b005b341561026757600080fd5b6102b7600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610aed565b005b34156102c457600080fd5b6102cc610d7b565b6040518082815260200191505060405180910390f35b34156102ed57600080fd5b610341600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050610d81565b604051808215151515815260200191505060405180910390f35b341561036657600080fd5b610392600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050611105565b005b341561039f57600080fd5b6103a76111a3565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103e75780820151818401526020810190506103cc565b50505050905090810190601f1680156104145780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561042d57600080fd5b610459600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506111dc565b6040518082815260200191505060405180910390f35b341561047a57600080fd5b610482611225565b6040518082815260200191505060405180910390f35b34156104a357600080fd5b6104ab61122b565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156104f857600080fd5b610548600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050611250565b005b341561055557600080fd5b6105a5600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050611253565b005b34156105b257600080fd5b6105e7600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919080359060200190919050506112e2565b005b34156105f457600080fd5b610629600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919080359060200190919050506113ca565b604051808215151515815260200191505060405180910390f35b341561064e57600080fd5b610656611612565b005b341561066357600080fd5b6106db600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506116b9565b604051808215151515815260200191505060405180910390f35b341561070057600080fd5b610750600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050611814565b005b341561075d57600080fd5b6107a8600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061182e565b6040518082815260200191505060405180910390f35b34156107c957600080fd5b6107d16118b5565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108a95780601f1061087e576101008083540402835291602001916108a9565b820191906000526020600020905b81548152906001019060200180831161088c57829003601f168201915b505050505081565b6000808373ffffffffffffffffffffffffffffffffffffffff16141515156108d857600080fd5b600082148061096357506000600a60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054145b151561096e57600080fd5b81600a60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905092915050565b600080600080602185015193506041850151925060025484121515610a895760025484039150610a91565b836002540391505b60008212151515610a9e57fe5b60035483121515610ab55760035483039050610abd565b826003540390505b60008112151515610aca57fe5b6103e8821380610adb57506103e881135b1515610ae657600080fd5b5050505050565b600081511415610b0457610aff611612565b610d78565b60017f010000000000000000000000000000000000000000000000000000000000000002816000815181101515610b3757fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415610bb857610bb381611253565b610d77565b60027f010000000000000000000000000000000000000000000000000000000000000002816000815181101515610beb57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415610cb3576000600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610ca557600080fd5b610cae81610a5e565b610d76565b60037f010000000000000000000000000000000000000000000000000000000000000002816000815181101515610ce657fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19161415610d6757610d6281611250565b610d75565b60001515610d7457600080fd5b5b5b5b5b50565b60085481565b6000808473ffffffffffffffffffffffffffffffffffffffff1614151515610da857600080fd5b60008373ffffffffffffffffffffffffffffffffffffffff1614151515610dce57600080fd5b3073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614151515610e0957600080fd5b81600960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610e5757600080fd5b81600a60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410151515610ee257600080fd5b600960008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482600960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020540110151515610f7157600080fd5b81600960008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254019250508190555081600960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600a60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825403925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a3600190509392505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561116057600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6040805190810160405280600581526020017f302e312e3100000000000000000000000000000000000000000000000000000081525081565b6000600960008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b60055481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b50565b6000806000611261846118db565b8093508194508295505050503373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415156112a757600080fd5b428167ffffffffffffffff161015156112bf57600080fd5b6005548167ffffffffffffffff164203111515156112dc57600080fd5b50505050565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561133e57600080fd5b42600660008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555080600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055505050565b6000808373ffffffffffffffffffffffffffffffffffffffff16141515156113f157600080fd5b3073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415151561142c57600080fd5b81600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561147a57600080fd5b600960008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482600960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054011015151561150957600080fd5b81600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600960008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a36001905092915050565b6000600660003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000811415151561166657600080fd5b600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054814203111515156116b657600080fd5b50565b60008060006116c886866113ca565b15156116d357600080fd5b853b91506000821115611807578590508073ffffffffffffffffffffffffffffffffffffffff1663c0ee0b8a3387876040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156117a557808201518184015260208101905061178a565b50505050905090810190601f1680156117d25780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b15156117f257600080fd5b6102c65a03f1151561180357600080fd5b5050505b6001925050509392505050565b8061181e81610aed565b6118293360646113ca565b505050565b6000600a60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806000806118e9611bfc565b6118f1611bfc565b60008060008060008b51945060418503975061190e8c8987611a97565b965061191c8c60018a611a97565b955061192787611ba2565b935093509350856040518082805190602001908083835b602083101515611963578051825260208201915060208101905060208303925061193e565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405180910390209050600181838686604051600081526020016040526000604051602001526040518085600019166000191681526020018460ff1660ff16815260200183600019166000191681526020018260001916600019168152602001945050505050602060405160208103908084039060008661646e5a03f11515611a1057600080fd5b5050602060405103519a50600460009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168b73ffffffffffffffffffffffffffffffffffffffff16141515611a7757600080fd5b611a8086611be6565b809a50819b50505050505050505050509193909250565b611a9f611bfc565b600082855110151515611ab157600080fd5b60008410151515611ac157600080fd5b838303604051805910611ad15750595b9080825280601f01601f191660200182016040525091508390505b82811015611b9a578481815181101515611b0257fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000282858303815181101515611b5d57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050611aec565b509392505050565b6000806000602084015192506040840151915060ff6041850151169050601b8160ff161480611bd45750601c8160ff16145b1515611bdf57600080fd5b9193909250565b60008060148301519150601c8301519050915091565b6020604051908101604052806000815250905600a165627a7a723058209ca38ad6d16030022687c4db8ab9718ed41e5104bb17642510c8c1ca76bdb8820029".hexStringToByteArray()

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



