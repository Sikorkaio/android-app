// This file is an automatically generated Java binding. Do not modify as any
// change will likely be lost upon the next re-generation!

package io.sikorka.android.contract;


import org.ethereum.geth.BigInt;
import org.ethereum.geth.BoundContract;
import org.ethereum.geth.CallOpts;
import org.ethereum.geth.EthereumClient;
import org.ethereum.geth.Geth;
import org.ethereum.geth.Interface;
import org.ethereum.geth.Interfaces;
import org.ethereum.geth.TransactOpts;
import org.ethereum.geth.Transaction;
import org.ethereum.geth.Address;

public class Owned {
    // ABI is the input ABI used to generate the binding from.
    public final static String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"_newOwner\",\"type\":\"address\"}],\"name\":\"change_owner\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";


    // BYTECODE is the compiled bytecode used for deploying new contracts.
    public final static byte[] BYTECODE = "0x6060604052341561000f57600080fd5b5b60008054600160a060020a03191633600160a060020a03161790555b5b60f48061003b6000396000f300606060405263ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663253c8bd48114603c575b600080fd5b3415604657600080fd5b606573ffffffffffffffffffffffffffffffffffffffff600435166067565b005b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614608e57600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff191673ffffffffffffffffffffffffffffffffffffffff83161790555b5b505600a165627a7a723058202633c19e2e33f96daed8797d97da5fd154ec9a053e432826cfebbe53072ee32b0029".getBytes();
    // Ethereum address where this contract is located at.
    public final Address Address;
    // Ethereum transaction in which this contract was deployed (if known!).
    public final Transaction Deployer;
    // Contract instance bound to a blockchain address.
    private final BoundContract Contract;

    // Internal constructor used by contract deployment.
    private Owned(BoundContract deployment) {
        this.Address = deployment.getAddress();
        this.Deployer = deployment.getDeployer();
        this.Contract = deployment;
    }

    // Creates a new instance of Owned, bound to a specific deployed contract.
    public Owned(Address address, EthereumClient client) throws Exception {
        this(Geth.bindContract(address, ABI, client));
    }

    // deploy deploys a new Ethereum contract, binding an instance of Owned to it.
    public static Owned deploy(TransactOpts auth, EthereumClient client) throws Exception {
        Interfaces args = Geth.newInterfaces(0);

        return new Owned(Geth.deployContract(auth, ABI, BYTECODE, client, args));
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

}

