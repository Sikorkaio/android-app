package io.sikorka.android.contract;

import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.BoundContract;
import org.ethereum.geth.CallOpts;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Interface;
import org.ethereum.geth.Interfaces;
import org.ethereum.geth.TransactOpts;
import org.ethereum.geth.Transaction;

import static io.sikorka.android.helpers.HexByteUtilKt.hexStringToByteArray;

public class SikorkaBasicInterface {
  // ABI is the input ABI used to generate the binding from.
  public final static String ABI = "[{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_newOwner\",\"type\":\"address\"}],\"name\":\"change_owner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"question\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_question\",\"type\":\"string\"},{\"name\":\"_answer_hash\",\"type\":\"bytes32\"}],\"name\":\"change_question\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"_answer\",\"type\":\"string\"}],\"name\":\"confirm_answer\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_latitude\",\"type\":\"uint256\"},{\"name\":\"_longtitude\",\"type\":\"uint256\"},{\"name\":\"_question\",\"type\":\"string\"},{\"name\":\"_answer_hash\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";


  // BYTECODE is the compiled bytecode used for deploying new contracts.
  public final static byte[] BYTECODE = hexStringToByteArray("0x6060604052341561000f57600080fd5b6040516106dc3803806106dc8339810160405280805182019190602001805191906020018051919060200180518201919060200180519150505b5b60008054600160a060020a03191633600160a060020a03161790555b60018580516100799291602001906100a8565b506004849055600583905560028280516100979291602001906100a8565b5060038190555b5050505050610148565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100e957805160ff1916838001178555610116565b82800160010185558215610116579182015b828111156101165782518255916020019190600101906100fb565b5b50610123929150610127565b5090565b61014591905b80821115610123576000815560010161012d565b5090565b90565b610585806101576000396000f300606060405263ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde038114610069578063253c8bd4146100f45780633fad9ae0146101225780635b97fb52146101ad578063f2e5dacc14610202575b600080fd5b341561007457600080fd5b61007c610267565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100b95780820151818401525b6020016100a0565b50505050905090810190601f1680156100e65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156100ff57600080fd5b61012073ffffffffffffffffffffffffffffffffffffffff60043516610305565b005b341561012d57600080fd5b61007c610367565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156100b95780820151818401525b6020016100a0565b50505050905090810190601f1680156100e65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101b857600080fd5b61012060046024813581810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650509335935061040592505050565b005b341561020d57600080fd5b61025360046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284375094965061044c95505050505050565b604051901515815260200160405180910390f35b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102fd5780601f106102d2576101008083540402835291602001916102fd565b820191906000526020600020905b8154815290600101906020018083116102e057829003601f168201915b505050505081565b6000543373ffffffffffffffffffffffffffffffffffffffff90811691161461032d57600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff191673ffffffffffffffffffffffffffffffffffffffff83161790555b5b50565b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102fd5780601f106102d2576101008083540402835291602001916102fd565b820191906000526020600020905b8154815290600101906020018083116102e057829003601f168201915b505050505081565b6000543373ffffffffffffffffffffffffffffffffffffffff90811691161461042d57600080fd5b60028280516104409291602001906104b9565b5060038190555b5b5050565b600354600090826040518082805190602001908083835b6020831061048357805182525b601f199092019160209182019101610463565b6001836020036101000a03801982511681845116179092525050509190910192506040915050519081900390201490505b919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106104fa57805160ff1916838001178555610527565b82800160010185558215610527579182015b8281111561052757825182559160200191906001019061050c565b5b50610534929150610538565b5090565b61055691905b80821115610534576000815560010161053e565b5090565b905600a165627a7a72305820be498bbd9d007b20a22ed49c3e23fa49d0ce586063f2491709f082565912b4170029");
  // Ethereum address where this contract is located at.
  public final org.ethereum.geth.Address Address;
  // Ethereum transaction in which this contract was deployed (if known!).
  public final Transaction Deployer;
  // Contract instance bound to a blockchain address.
  private final BoundContract Contract;

  // Internal constructor used by contract deployment.
  private SikorkaBasicInterface(BoundContract deployment) {
    this.Address = deployment.getAddress();
    this.Deployer = deployment.getDeployer();
    this.Contract = deployment;
  }

  // Creates a new instance of SikorkaBasicInterface, bound to a specific deployed contract.
  public SikorkaBasicInterface(Address address, EthereumClient client) throws Exception {
    this(Geth.bindContract(address, ABI, client));
  }

  // deploy deploys a new Ethereum contract, binding an instance of SikorkaBasicInterface to it.
  public static SikorkaBasicInterface deploy(TransactOpts auth, EthereumClient client, String _name, BigInt _latitude, BigInt _longtitude, String _question, byte[] _answer_hash) throws Exception {
    Interfaces args = Geth.newInterfaces(5);

    Interface name = Geth.newInterface();
    name.setString(_name);
    args.set(0, name);

    Interface lat = Geth.newInterface();
    lat.setBigInt(_latitude);
    args.set(1, lat);

    Interface longi = Geth.newInterface();
    longi.setBigInt(_longtitude);
    args.set(2, longi);

    Interface quest = Geth.newInterface();
    quest.setString(_question);
    args.set(3, quest);

    Interface hash = Geth.newInterface();
    hash.setBinary(_answer_hash);
    args.set(4, hash);

    return new SikorkaBasicInterface(Geth.deployContract(auth, ABI, BYTECODE, client, args));
  }

  // confirm_answer is a free data retrieval call binding the contract method 0xf2e5dacc.
  //
  // Solidity: function confirm_answer(_answer string) constant returns(bool)
  public boolean confirm_answer(CallOpts opts, String _answer) throws Exception {
    Interfaces args = Geth.newInterfaces(1);
    args.set(0, Geth.newInterface());
    args.get(0).setString(_answer);


    Interfaces results = Geth.newInterfaces(1);
    Interface result0 = Geth.newInterface();
    result0.setDefaultBool();
    results.set(0, result0);


    if (opts == null) {
      opts = Geth.newCallOpts();
    }
    this.Contract.call(opts, results, "confirm_answer", args);
    return results.get(0).getBool();

  }


  // name is a free data retrieval call binding the contract method 0x06fdde03.
  //
  // Solidity: function name() constant returns(string)
  public String name(CallOpts opts) throws Exception {
    Interfaces args = Geth.newInterfaces(0);


    Interfaces results = Geth.newInterfaces(1);
    Interface result0 = Geth.newInterface();
    result0.setDefaultString();
    results.set(0, result0);


    if (opts == null) {
      opts = Geth.newCallOpts();
    }
    this.Contract.call(opts, results, "name", args);
    return results.get(0).getString();

  }


  // question is a free data retrieval call binding the contract method 0x3fad9ae0.
  //
  // Solidity: function question() constant returns(string)
  public String question(CallOpts opts) throws Exception {
    Interfaces args = Geth.newInterfaces(0);


    Interfaces results = Geth.newInterfaces(1);
    Interface result0 = Geth.newInterface();
    result0.setDefaultString();
    results.set(0, result0);


    if (opts == null) {
      opts = Geth.newCallOpts();
    }
    this.Contract.call(opts, results, "question", args);
    return results.get(0).getString();

  }


  // change_owner is a paid mutator transaction binding the contract method 0x253c8bd4.
  //
  // Solidity: function change_owner(_newOwner address) returns()
  public Transaction change_owner(TransactOpts opts, Address _newOwner) throws Exception {
    Interfaces args = Geth.newInterfaces(1);
    args.set(0, Geth.newInterface());
    args.get(0).setAddress(_newOwner);


    return this.Contract.transact(opts, "change_owner", args);
  }

  // change_question is a paid mutator transaction binding the contract method 0x5b97fb52.
  //
  // Solidity: function change_question(_question string, _answer_hash bytes32) returns()
  public Transaction change_question(TransactOpts opts, String _question, byte[] _answer_hash) throws Exception {
    Interfaces args = Geth.newInterfaces(2);
    args.set(0, Geth.newInterface());
    args.get(0).setString(_question);
    args.set(1, Geth.newInterface());
    args.get(1).setBinary(_answer_hash);


    return this.Contract.transact(opts, "change_question", args);
  }

}
