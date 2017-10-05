package io.sikorka.android.contract

import io.sikorka.android.helpers.hexStringToByteArray
import org.ethereum.geth.*

interface ISikorkaBasicInterface {
  /**
   * confirm_answer is a free data retrieval call binding the contract method 0xf2e5dacc.
   *
   * ```Solidity: function confirm_answer(_answer string) constant returns(bool)```
   */
  fun confirmAnswer(opts: CallOpts?, _answer: String): Boolean

  /**
   * name is a free data retrieval call binding the contract method 0x06fdde03.
   *
   * ```Solidity: function name() constant returns(string)```
   */
  fun name(opts: CallOpts? = null): String

  /**
   * question is a free data retrieval call binding the contract method 0x3fad9ae0.
   *
   * ```Solidity: function question() constant returns(string)```
   */
  fun question(opts: CallOpts? = null): String

  /**
   * change_owner is a paid mutator transaction binding the contract method 0x253c8bd4.
   *
   * ```Solidity: function change_owner(_newOwner address) returns()```
   */
  fun changeOwner(opts: TransactOpts, _newOwner: Address): Transaction

  /**
   * change_question is a paid mutator transaction binding the contract method 0x5b97fb52.
   *
   * ```Solidity: function change_question(_question string, _answer_hash bytes32) returns()```
   */
  fun changeQuestion(opts: TransactOpts, _question: String, _answer_hash: ByteArray): Transaction
}

/**
 * Internal constructor used by contract deployment.
 *
 * @property contract instance bound to a blockchain address
 */
class SikorkaBasicInterface
private constructor(
    private val contract: BoundContract
) : ISikorkaBasicInterface {
  /**
   * Ethereum address where this contract is located at.
   */
  val address: Address = contract.address
  /**
   * Ethereum transaction in which this contract was deployed (if known!).
   */
  val deployer: Transaction? = contract.deployer

  /**
   * Creates a new instance of SikorkaBasicInterface, bound to a specific deployed contract.
   */
  constructor(address: Address, client: EthereumClient) : this(Geth.bindContract(address, ABI, client))

  // confirm_answer is a free data retrieval call binding the contract method 0xf2e5dacc.
  //
  // Solidity: function confirm_answer(_answer string) constant returns(bool)
  @Throws(Exception::class)
  override fun confirmAnswer(opts: CallOpts?, _answer: String): Boolean {
    var callOpts = opts
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).string = _answer


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultBool()
    results.set(0, result0)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "confirm_answer", args)
    return results.get(0).bool

  }


  // name is a free data retrieval call binding the contract method 0x06fdde03.
  //
  // Solidity: function name() constant returns(string)
  @Throws(Exception::class)
  override fun name(opts: CallOpts?): String {
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


  // question is a free data retrieval call binding the contract method 0x3fad9ae0.
  //
  // Solidity: function question() constant returns(string)
  @Throws(Exception::class)
  override fun question(opts: CallOpts?): String {
    var callOpts = opts
    val args = Geth.newInterfaces(0)


    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)


    if (callOpts == null) {
      callOpts = Geth.newCallOpts()
    }
    this.contract.call(callOpts, results, "question", args)
    return results.get(0).string

  }


  // change_owner is a paid mutator transaction binding the contract method 0x253c8bd4.
  //
  // Solidity: function change_owner(_newOwner address) returns()
  @Throws(Exception::class)
  override fun changeOwner(opts: TransactOpts, _newOwner: Address): Transaction {
    val args = Geth.newInterfaces(1)
    args.set(0, Geth.newInterface())
    args.get(0).address = _newOwner


    return this.contract.transact(opts, "change_owner", args)
  }

  // change_question is a paid mutator transaction binding the contract method 0x5b97fb52.
  //
  // Solidity: function change_question(_question string, _answer_hash bytes32) returns()
  @Throws(Exception::class)
  override fun changeQuestion(opts: TransactOpts, _question: String, _answer_hash: ByteArray): Transaction {
    val args = Geth.newInterfaces(2)
    args.set(0, Geth.newInterface())
    args.get(0).string = _question
    args.set(1, Geth.newInterface())
    args.get(1).binary = _answer_hash


    return this.contract.transact(opts, "change_question", args)
  }

  companion object {
    /**
     * ABI is the input ABI used to generate the binding from.
     */
    const val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"question","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_question","type":"string"},{"name":"_answer_hash","type":"bytes32"}],"name":"change_question","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_answer","type":"string"}],"name":"confirm_answer","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_latitude","type":"uint256"},{"name":"_longtitude","type":"uint256"},{"name":"_question","type":"string"},{"name":"_answer_hash","type":"bytes32"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"}]"""


    /**
     * BYTECODE is the compiled bytecode used for deploying new contracts.
     */
    val BYTECODE = "6060604052341561000f57600080fd5b6040516107d83803806107d883398101604052808051820191906020018051906020019091908051906020019091908051820191906020018051906020019091905050336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555084600190805190602001906100a89291906100e3565b50836004819055508260058190555081600290805190602001906100cd9291906100e3565b5080600381600019169055505050505050610188565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061012457805160ff1916838001178555610152565b82800160010185558215610152579182015b82811115610151578251825591602001919060010190610136565b5b50905061015f9190610163565b5090565b61018591905b80821115610181576000816000905550600101610169565b5090565b90565b610641806101976000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806306fdde0314610069578063253c8bd4146100f75780633fad9ae0146101305780635b97fb52146101be578063f2e5dacc1461022857600080fd5b341561007457600080fd5b61007c61029d565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100bc5780820151818401526020810190506100a1565b50505050905090810190601f1680156100e95780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561010257600080fd5b61012e600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061033b565b005b341561013b57600080fd5b6101436103d9565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610183578082015181840152602081019050610168565b50505050905090810190601f1680156101b05780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101c957600080fd5b610226600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803560001916906020019091905050610477565b005b341561023357600080fd5b610283600480803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506104f8565b604051808215151515815260200191505060405180910390f35b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103335780601f1061030857610100808354040283529160200191610333565b820191906000526020600020905b81548152906001019060200180831161031657829003601f168201915b505050505081565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561039657600080fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60028054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561046f5780601f106104445761010080835404028352916020019161046f565b820191906000526020600020905b81548152906001019060200180831161045257829003601f168201915b505050505081565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156104d257600080fd5b81600290805190602001906104e8929190610570565b5080600381600019169055505050565b600060035460001916826040518082805190602001908083835b6020831015156105375780518252602082019150602081019050602083039250610512565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916149050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105b157805160ff19168380011785556105df565b828001600101855582156105df579182015b828111156105de5782518255916020019190600101906105c3565b5b5090506105ec91906105f0565b5090565b61061291905b8082111561060e5760008160009055506001016105f6565b5090565b905600a165627a7a723058206577bd19d55411971703d37f17bec4d5a6cc4230b632e89fcca383142e871d150029".hexStringToByteArray()

    // deploy deploys a new Ethereum contract, binding an instance of SikorkaBasicInterface to it.
    @Throws(Exception::class)
    fun deploy(auth: TransactOpts, client: EthereumClient, _name: String, _latitude: BigInt, _longtitude: BigInt, _question: String, _answer_hash: ByteArray): SikorkaBasicInterface {
      val args = Geth.newInterfaces(5)

      val name = Geth.newInterface()
      name.string = _name
      args.set(0, name)

      val lat = Geth.newInterface()
      lat.bigInt = _latitude
      args.set(1, lat)

      val longi = Geth.newInterface()
      longi.bigInt = _longtitude
      args.set(2, longi)

      val quest = Geth.newInterface()
      quest.string = _question
      args.set(3, quest)

      val hash = Geth.newInterface()
      hash.binary = _answer_hash
      args.set(4, hash)

      return SikorkaBasicInterface(Geth.deployContract(auth, ABI, BYTECODE, client, args))
    }
  }

}
