// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!

package io.sikorka.android.contract

import io.sikorka.android.helpers.hexStringToByteArray
import org.ethereum.geth.Address
import org.ethereum.geth.BigInt
import org.ethereum.geth.BoundContract
import org.ethereum.geth.CallOpts
import org.ethereum.geth.EthereumClient
import org.ethereum.geth.Geth
import org.ethereum.geth.TransactOpts
import org.ethereum.geth.Transaction

@Suppress("unused")
class DiscountContract
private constructor(private val contract: BoundContract) {

  // Ethereum address where this contract is located at.
  val address: Address = contract.address

  // Ethereum transaction in which this contract was deployed (if known!).
  val deployer: Transaction? = contract.deployer

  // Creates a new instance of DiscountContract, bound to a specific deployed contract.
  @Throws(Exception::class)
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(
    address,
    ABI,
    client
  ))

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
  fun detector(opts: CallOpts? = null): Address {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultAddress()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "detector", args)
    return results.get(0).address
  }

  // name is a free data retrieval call binding the contract method 0x06fdde03.
  //
  // Solidity: function name() constant returns(string)
  @Throws(Exception::class)
  fun name(opts: CallOpts? = null): String {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "name", args)
    return results.get(0).string
  }

  // owner is a free data retrieval call binding the contract method 0x8da5cb5b.
  //
  // Solidity: function owner() constant returns(address)
  @Throws(Exception::class)
  fun owner(opts: CallOpts? = null): Address {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultAddress()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "owner", args)
    return results.get(0).address
  }

  // seconds_allowed is a free data retrieval call binding the contract method 0x78147881.
  //
  // Solidity: function seconds_allowed() constant returns(uint256)
  @Throws(Exception::class)
  fun secondsAllowed(opts: CallOpts? = null): BigInt {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "seconds_allowed", args)
    return results.get(0).bigInt
  }

  // totalSupply is a free data retrieval call binding the contract method 0x18160ddd.
  //
  // Solidity: function totalSupply() constant returns(uint256)
  @Throws(Exception::class)
  fun totalSupply(opts: CallOpts? = null): BigInt {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBigInt()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "totalSupply", args)
    return results.get(0).bigInt
  }

  // version is a free data retrieval call binding the contract method 0x54fd4d50.
  //
  // Solidity: function version() constant returns(string)
  @Throws(Exception::class)
  fun version(opts: CallOpts? = null): String {
    var callOpts = opts
    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)

    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "version", args)
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
  fun authorizeUser(opts: TransactOpts, user: Address, duration: BigInt): Transaction {
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
  fun changeOwner(opts: TransactOpts, _newOwner: Address): Transaction {
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
    const val ABI = """[
  {
    "constant": true,
    "inputs": [],
    "name": "name",
    "outputs": [
      {
        "name": "",
        "type": "string"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "_spender",
        "type": "address"
      },
      {
        "name": "_value",
        "type": "uint256"
      }
    ],
    "name": "approve",
    "outputs": [
      {
        "name": "",
        "type": "bool"
      }
    ],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "message",
        "type": "bytes"
      }
    ],
    "name": "simple_presence_check",
    "outputs": [],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "message",
        "type": "bytes"
      }
    ],
    "name": "check_proof_of_presence",
    "outputs": [],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "totalSupply",
    "outputs": [
      {
        "name": "",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "_from",
        "type": "address"
      },
      {
        "name": "_to",
        "type": "address"
      },
      {
        "name": "_value",
        "type": "uint256"
      }
    ],
    "name": "transferFrom",
    "outputs": [
      {
        "name": "",
        "type": "bool"
      }
    ],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "_newOwner",
        "type": "address"
      }
    ],
    "name": "change_owner",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "version",
    "outputs": [
      {
        "name": "",
        "type": "string"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "_owner",
        "type": "address"
      }
    ],
    "name": "balanceOf",
    "outputs": [
      {
        "name": "",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "seconds_allowed",
    "outputs": [
      {
        "name": "",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "owner",
    "outputs": [
      {
        "name": "",
        "type": "address"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "message",
        "type": "bytes"
      }
    ],
    "name": "signed_timestamp_check",
    "outputs": [],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "message",
        "type": "bytes"
      }
    ],
    "name": "detector_signed_message",
    "outputs": [],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "user",
        "type": "address"
      },
      {
        "name": "duration",
        "type": "uint256"
      }
    ],
    "name": "authorize_user",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "_to",
        "type": "address"
      },
      {
        "name": "_value",
        "type": "uint256"
      }
    ],
    "name": "transfer",
    "outputs": [
      {
        "name": "",
        "type": "bool"
      }
    ],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "detector_direct_authorization",
    "outputs": [],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "_to",
        "type": "address"
      },
      {
        "name": "_value",
        "type": "uint256"
      },
      {
        "name": "_data",
        "type": "bytes"
      }
    ],
    "name": "transfer",
    "outputs": [
      {
        "name": "",
        "type": "bool"
      }
    ],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": false,
    "inputs": [
      {
        "name": "data",
        "type": "bytes"
      }
    ],
    "name": "claimTokens",
    "outputs": [],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [
      {
        "name": "_owner",
        "type": "address"
      },
      {
        "name": "_spender",
        "type": "address"
      }
    ],
    "name": "allowance",
    "outputs": [
      {
        "name": "",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "constant": true,
    "inputs": [],
    "name": "detector",
    "outputs": [
      {
        "name": "",
        "type": "address"
      }
    ],
    "payable": false,
    "stateMutability": "view",
    "type": "function"
  },
  {
    "inputs": [
      {
        "name": "_name",
        "type": "string"
      },
      {
        "name": "_detector",
        "type": "address"
      },
      {
        "name": "_latitude",
        "type": "int256"
      },
      {
        "name": "_longitude",
        "type": "int256"
      },
      {
        "name": "_seconds_allowed",
        "type": "uint256"
      },
      {
        "name": "_registry_address",
        "type": "address"
      },
      {
        "name": "_totalSupply",
        "type": "uint256"
      }
    ],
    "payable": false,
    "stateMutability": "nonpayable",
    "type": "constructor"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": true,
        "name": "_from",
        "type": "address"
      },
      {
        "indexed": true,
        "name": "_to",
        "type": "address"
      },
      {
        "indexed": false,
        "name": "_value",
        "type": "uint256"
      }
    ],
    "name": "Transfer",
    "type": "event"
  },
  {
    "anonymous": false,
    "inputs": [
      {
        "indexed": true,
        "name": "_owner",
        "type": "address"
      },
      {
        "indexed": true,
        "name": "_spender",
        "type": "address"
      },
      {
        "indexed": false,
        "name": "_value",
        "type": "uint256"
      }
    ],
    "name": "Approval",
    "type": "event"
  }
]"""

    // BYTECODE is the compiled bytecode used for deploying new contracts.
    private val BYTECODE = (
      "606060405234156200001057600080fd5b60405162001f3b38038062001f3b83" +
        "3981016040528080518201919060200180519060200190919080519060200190" +
        "9190805190602001909190805190602001909190805190602001909190805190" +
        "60200190919050508686868686866000336000806101000a81548173ffffffff" +
        "ffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffff" +
        "ffffffffffffffffffff1602179055508660019080519060200190620000c792" +
        "919062000240565b508460028190555083600381905550826005819055508560" +
        "0460006101000a81548173ffffffffffffffffffffffffffffffffffffffff02" +
        "1916908373ffffffffffffffffffffffffffffffffffffffff16021790555081" +
        "90508073ffffffffffffffffffffffffffffffffffffffff16637ca8f9113087" +
        "876040518463ffffffff167c0100000000000000000000000000000000000000" +
        "000000000000000000028152600401808473ffffffffffffffffffffffffffff" +
        "ffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260" +
        "2001838152602001828152602001935050505060006040518083038160008780" +
        "3b1515620001cc57600080fd5b6102c65a03f11515620001de57600080fd5b50" +
        "5050505050505050508060088190555080600960003073ffffffffffffffffff" +
        "ffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffff" +
        "ff1681526020019081526020016000208190555050505050505050620002ef56" +
        "5b82805460018160011615610100020316600290049060005260206000209060" +
        "1f016020900481019282601f106200028357805160ff19168380011785556200" +
        "02b4565b82800160010185558215620002b4579182015b82811115620002b357" +
        "825182559160200191906001019062000296565b5b509050620002c391906200" +
        "02c7565b5090565b620002ec91905b80821115620002e8576000816000905550" +
        "600101620002ce565b5090565b90565b611c3c80620002ff6000396000f30060" +
        "6060405260043610610112576000357c01000000000000000000000000000000" +
        "00000000000000000000000000900463ffffffff16806306fdde031461011757" +
        "8063095ea7b3146101a557806315ace0a0146101ff57806316b205511461025c" +
        "57806318160ddd146102b957806323b872dd146102e2578063253c8bd4146103" +
        "5b57806354fd4d501461039457806370a0823114610422578063781478811461" +
        "046f5780638da5cb5b146104985780638f3e9d79146104ed57806397ac983114" +
        "61054a5780639f60eef6146105a7578063a9059cbb146105e9578063b6ac0959" +
        "14610643578063be45fd6214610658578063dac90e64146106f5578063dd62ed" +
        "3e14610752578063e4e20d63146107be575b600080fd5b341561012257600080" +
        "fd5b61012a610813565b60405180806020018281038252838181518152602001" +
        "91508051906020019080838360005b8381101561016a57808201518184015260" +
        "208101905061014f565b50505050905090810190601f16801561019757808203" +
        "80516001836020036101000a031916815260200191505b509250505060405180" +
        "910390f35b34156101b057600080fd5b6101e5600480803573ffffffffffffff" +
        "ffffffffffffffffffffffffff16906020019091908035906020019091905050" +
        "6108b1565b604051808215151515815260200191505060405180910390f35b34" +
        "1561020a57600080fd5b61025a60048080359060200190820180359060200190" +
        "8080601f01602080910402602001604051908101604052809392919081815260" +
        "200183838082843782019150505050505091905050610a5e565b005b34156102" +
        "6757600080fd5b6102b760048080359060200190820180359060200190808060" +
        "1f01602080910402602001604051908101604052809392919081815260200183" +
        "838082843782019150505050505091905050610aed565b005b34156102c45760" +
        "0080fd5b6102cc610d7b565b6040518082815260200191505060405180910390" +
        "f35b34156102ed57600080fd5b610341600480803573ffffffffffffffffffff" +
        "ffffffffffffffffffff1690602001909190803573ffffffffffffffffffffff" +
        "ffffffffffffffffff16906020019091908035906020019091905050610d8156" +
        "5b604051808215151515815260200191505060405180910390f35b3415610366" +
        "57600080fd5b610392600480803573ffffffffffffffffffffffffffffffffff" +
        "ffffff16906020019091905050611105565b005b341561039f57600080fd5b61" +
        "03a76111a3565b60405180806020018281038252838181518152602001915080" +
        "51906020019080838360005b838110156103e757808201518184015260208101" +
        "90506103cc565b50505050905090810190601f16801561041457808203805160" +
        "01836020036101000a031916815260200191505b509250505060405180910390" +
        "f35b341561042d57600080fd5b610459600480803573ffffffffffffffffffff" +
        "ffffffffffffffffffff169060200190919050506111dc565b60405180828152" +
        "60200191505060405180910390f35b341561047a57600080fd5b610482611225" +
        "565b6040518082815260200191505060405180910390f35b34156104a3576000" +
        "80fd5b6104ab61122b565b604051808273ffffffffffffffffffffffffffffff" +
        "ffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020" +
        "0191505060405180910390f35b34156104f857600080fd5b6105486004808035" +
        "90602001908201803590602001908080601f0160208091040260200160405190" +
        "8101604052809392919081815260200183838082843782019150505050505091" +
        "905050611250565b005b341561055557600080fd5b6105a56004808035906020" +
        "01908201803590602001908080601f0160208091040260200160405190810160" +
        "4052809392919081815260200183838082843782019150505050505091905050" +
        "611253565b005b34156105b257600080fd5b6105e7600480803573ffffffffff" +
        "ffffffffffffffffffffffffffffff1690602001909190803590602001909190" +
        "50506112e2565b005b34156105f457600080fd5b610629600480803573ffffff" +
        "ffffffffffffffffffffffffffffffffff169060200190919080359060200190" +
        "919050506113ca565b6040518082151515158152602001915050604051809103" +
        "90f35b341561064e57600080fd5b610656611612565b005b3415610663576000" +
        "80fd5b6106db600480803573ffffffffffffffffffffffffffffffffffffffff" +
        "1690602001909190803590602001909190803590602001908201803590602001" +
        "908080601f016020809104026020016040519081016040528093929190818152" +
        "602001838380828437820191505050505050919050506116b9565b6040518082" +
        "15151515815260200191505060405180910390f35b341561070057600080fd5b" +
        "610750600480803590602001908201803590602001908080601f016020809104" +
        "0260200160405190810160405280939291908181526020018383808284378201" +
        "9150505050505091905050611814565b005b341561075d57600080fd5b6107a8" +
        "600480803573ffffffffffffffffffffffffffffffffffffffff169060200190" +
        "9190803573ffffffffffffffffffffffffffffffffffffffff16906020019091" +
        "90505061182e565b6040518082815260200191505060405180910390f35b3415" +
        "6107c957600080fd5b6107d16118b5565b604051808273ffffffffffffffffff" +
        "ffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffff" +
        "ff16815260200191505060405180910390f35b60018054600181600116156101" +
        "000203166002900480601f016020809104026020016040519081016040528092" +
        "9190818152602001828054600181600116156101000203166002900480156108" +
        "a95780601f1061087e576101008083540402835291602001916108a9565b8201" +
        "91906000526020600020905b81548152906001019060200180831161088c5782" +
        "9003601f168201915b505050505081565b6000808373ffffffffffffffffffff" +
        "ffffffffffffffffffff16141515156108d857600080fd5b6000821480610963" +
        "57506000600a60003373ffffffffffffffffffffffffffffffffffffffff1673" +
        "ffffffffffffffffffffffffffffffffffffffff168152602001908152602001" +
        "60002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffff" +
        "ffffffffffffffffffffffffffffffffff168152602001908152602001600020" +
        "54145b151561096e57600080fd5b81600a60003373ffffffffffffffffffffff" +
        "ffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16" +
        "815260200190815260200160002060008573ffffffffffffffffffffffffffff" +
        "ffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260" +
        "2001908152602001600020819055508273ffffffffffffffffffffffffffffff" +
        "ffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5b" +
        "e1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9258460" +
        "40518082815260200191505060405180910390a36001905092915050565b6000" +
        "80600080602185015193506041850151925060025484121515610a8957600254" +
        "84039150610a91565b836002540391505b60008212151515610a9e57fe5b6003" +
        "5483121515610ab55760035483039050610abd565b826003540390505b600081" +
        "12151515610aca57fe5b6103e8821380610adb57506103e881135b1515610ae6" +
        "57600080fd5b5050505050565b600081511415610b0457610aff611612565b61" +
        "0d78565b60017f01000000000000000000000000000000000000000000000000" +
        "0000000000000002816000815181101515610b3757fe5b9060200101517f0100" +
        "0000000000000000000000000000000000000000000000000000000000009004" +
        "7f01000000000000000000000000000000000000000000000000000000000000" +
        "00027effffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
        "ffff19161415610bb857610bb381611253565b610d77565b60027f0100000000" +
        "0000000000000000000000000000000000000000000000000000000281600081" +
        "5181101515610beb57fe5b9060200101517f0100000000000000000000000000" +
        "00000000000000000000000000000000000090047f0100000000000000000000" +
        "000000000000000000000000000000000000000000027effffffffffffffffff" +
        "ffffffffffffffffffffffffffffffffffffffffffff19161415610cb3576000" +
        "600460009054906101000a900473ffffffffffffffffffffffffffffffffffff" +
        "ffff1673ffffffffffffffffffffffffffffffffffffffff16141515610ca557" +
        "600080fd5b610cae81610a5e565b610d76565b60037f01000000000000000000" +
        "0000000000000000000000000000000000000000000002816000815181101515" +
        "610ce657fe5b9060200101517f01000000000000000000000000000000000000" +
        "0000000000000000000000000090047f01000000000000000000000000000000" +
        "00000000000000000000000000000000027effffffffffffffffffffffffffff" +
        "ffffffffffffffffffffffffffffffffff19161415610d6757610d6281611250" +
        "565b610d75565b60001515610d7457600080fd5b5b5b5b5b50565b6008548156" +
        "5b6000808473ffffffffffffffffffffffffffffffffffffffff161415151561" +
        "0da857600080fd5b60008373ffffffffffffffffffffffffffffffffffffffff" +
        "1614151515610dce57600080fd5b3073ffffffffffffffffffffffffffffffff" +
        "ffffffff168373ffffffffffffffffffffffffffffffffffffffff1614151515" +
        "610e0957600080fd5b81600960008673ffffffffffffffffffffffffffffffff" +
        "ffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001" +
        "9081526020016000205410151515610e5757600080fd5b81600a60008673ffff" +
        "ffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffff" +
        "ffffffffffffffff16815260200190815260200160002060003373ffffffffff" +
        "ffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffff" +
        "ffffffffff1681526020019081526020016000205410151515610ee257600080" +
        "fd5b600960008473ffffffffffffffffffffffffffffffffffffffff1673ffff" +
        "ffffffffffffffffffffffffffffffffffff1681526020019081526020016000" +
        "205482600960008673ffffffffffffffffffffffffffffffffffffffff1673ff" +
        "ffffffffffffffffffffffffffffffffffffff16815260200190815260200160" +
        "0020540110151515610f7157600080fd5b81600960008573ffffffffffffffff" +
        "ffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffff" +
        "ffff168152602001908152602001600020600082825401925050819055508160" +
        "0960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffff" +
        "ffffffffffffffffffffffffffffff1681526020019081526020016000206000" +
        "828254039250508190555081600a60008673ffffffffffffffffffffffffffff" +
        "ffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260" +
        "200190815260200160002060003373ffffffffffffffffffffffffffffffffff" +
        "ffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190" +
        "8152602001600020600082825403925050819055508273ffffffffffffffffff" +
        "ffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffff" +
        "ffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4d" +
        "f523b3ef846040518082815260200191505060405180910390a3600190509392" +
        "505050565b6000809054906101000a900473ffffffffffffffffffffffffffff" +
        "ffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ff" +
        "ffffffffffffffffffffffffffffffffffffff1614151561116057600080fd5b" +
        "806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff" +
        "021916908373ffffffffffffffffffffffffffffffffffffffff160217905550" +
        "50565b6040805190810160405280600581526020017f302e312e310000000000" +
        "0000000000000000000000000000000000000000000081525081565b60006009" +
        "60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffff" +
        "ffffffffffffffffffffffffffff168152602001908152602001600020549050" +
        "919050565b60055481565b6000809054906101000a900473ffffffffffffffff" +
        "ffffffffffffffffffffffff1681565b50565b6000806000611261846118db56" +
        "5b8093508194508295505050503373ffffffffffffffffffffffffffffffffff" +
        "ffffff168273ffffffffffffffffffffffffffffffffffffffff161415156112" +
        "a757600080fd5b428167ffffffffffffffff161015156112bf57600080fd5b60" +
        "05548167ffffffffffffffff164203111515156112dc57600080fd5b50505050" +
        "565b600460009054906101000a900473ffffffffffffffffffffffffffffffff" +
        "ffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffff" +
        "ffffffffffffffffffffffffffffffffff1614151561133e57600080fd5b4260" +
        "0660008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffff" +
        "ffffffffffffffffffffffffffffff1681526020019081526020016000208190" +
        "555080600760008473ffffffffffffffffffffffffffffffffffffffff1673ff" +
        "ffffffffffffffffffffffffffffffffffffff16815260200190815260200160" +
        "0020819055505050565b6000808373ffffffffffffffffffffffffffffffffff" +
        "ffffff16141515156113f157600080fd5b3073ffffffffffffffffffffffffff" +
        "ffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614" +
        "15151561142c57600080fd5b81600960003373ffffffffffffffffffffffffff" +
        "ffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152" +
        "602001908152602001600020541015151561147a57600080fd5b600960008473" +
        "ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffff" +
        "ffffffffffffffffffff16815260200190815260200160002054826009600086" +
        "73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffff" +
        "ffffffffffffffffffffff168152602001908152602001600020540110151515" +
        "61150957600080fd5b81600960003373ffffffffffffffffffffffffffffffff" +
        "ffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001" +
        "9081526020016000206000828254039250508190555081600960008573ffffff" +
        "ffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffff" +
        "ffffffffffffff16815260200190815260200160002060008282540192505081" +
        "9055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffff" +
        "ffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc37" +
        "8daa952ba7f163c4a11628f55a4df523b3ef8460405180828152602001915050" +
        "60405180910390a36001905092915050565b6000600660003373ffffffffffff" +
        "ffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffff" +
        "ffffffff16815260200190815260200160002054905060008114151515611666" +
        "57600080fd5b600760003373ffffffffffffffffffffffffffffffffffffffff" +
        "1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260" +
        "200160002054814203111515156116b657600080fd5b50565b60008060006116" +
        "c886866113ca565b15156116d357600080fd5b853b9150600082111561180757" +
        "8590508073ffffffffffffffffffffffffffffffffffffffff1663c0ee0b8a33" +
        "87876040518463ffffffff167c01000000000000000000000000000000000000" +
        "00000000000000000000028152600401808473ffffffffffffffffffffffffff" +
        "ffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152" +
        "6020018381526020018060200182810382528381815181526020019150805190" +
        "6020019080838360005b838110156117a5578082015181840152602081019050" +
        "61178a565b50505050905090810190601f1680156117d2578082038051600183" +
        "6020036101000a031916815260200191505b5094505050505060006040518083" +
        "0381600087803b15156117f257600080fd5b6102c65a03f11515611803576000" +
        "80fd5b5050505b6001925050509392505050565b8061181e81610aed565b6118" +
        "293360646113ca565b505050565b6000600a60008473ffffffffffffffffffff" +
        "ffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff" +
        "16815260200190815260200160002060008373ffffffffffffffffffffffffff" +
        "ffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152" +
        "60200190815260200160002054905092915050565b600460009054906101000a" +
        "900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806000" +
        "806118e9611bfc565b6118f1611bfc565b60008060008060008b519450604185" +
        "03975061190e8c8987611a97565b965061191c8c60018a611a97565b95506119" +
        "2787611ba2565b935093509350856040518082805190602001908083835b6020" +
        "8310151561196357805182526020820191506020810190506020830392506119" +
        "3e565b6001836020036101000a03801982511681845116808217855250505050" +
        "5050905001915050604051809103902090506001818386866040516000815260" +
        "2001604052600060405160200152604051808560001916600019168152602001" +
        "8460ff1660ff1681526020018360001916600019168152602001826000191660" +
        "0019168152602001945050505050602060405160208103908084039060008661" +
        "646e5a03f11515611a1057600080fd5b5050602060405103519a506004600090" +
        "54906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ff" +
        "ffffffffffffffffffffffffffffffffffffff168b73ffffffffffffffffffff" +
        "ffffffffffffffffffff16141515611a7757600080fd5b611a8086611be6565b" +
        "809a50819b50505050505050505050509193909250565b611a9f611bfc565b60" +
        "0082855110151515611ab157600080fd5b60008410151515611ac157600080fd" +
        "5b838303604051805910611ad15750595b9080825280601f01601f1916602001" +
        "82016040525091508390505b82811015611b9a578481815181101515611b0257" +
        "fe5b9060200101517f0100000000000000000000000000000000000000000000" +
        "00000000000000000090047f0100000000000000000000000000000000000000" +
        "0000000000000000000000000282858303815181101515611b5d57fe5b906020" +
        "0101907effffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
        "ffffff1916908160001a9053508080600101915050611aec565b509392505050" +
        "565b6000806000602084015192506040840151915060ff604185015116905060" +
        "1b8160ff161480611bd45750601c8160ff16145b1515611bdf57600080fd5b91" +
        "93909250565b60008060148301519150601c8301519050915091565b60206040" +
        "51908101604052806000815250905600a165627a7a723058209ca38ad6d16030" +
        "022687c4db8ab9718ed41e5104bb17642510c8c1ca76bdb8820029"
      ).hexStringToByteArray()

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