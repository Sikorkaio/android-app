package io.sikorka.android.contract

import org.ethereum.geth.BoundContract
import org.ethereum.geth.Geth

class BasicInterface(private val contract: BoundContract) {

  fun name(): String {
    val opts = Geth.newCallOpts()

    val args = Geth.newInterfaces(0)

    val results = Geth.newInterfaces(1)
    val result0 = Geth.newInterface()
    result0.setDefaultString()
    results.set(0, result0)

    this.contract.call(opts, results, "name", args)
    return results.get(0).string
  }

  companion object {
    const val ABI = """[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"simple_presence_check","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"check_proof_of_presence","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"change_owner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"seconds_allowed","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"signed_timestamp_check","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"message","type":"bytes"}],"name":"detector_signed_message","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"user","type":"address"},{"name":"duration","type":"uint256"}],"name":"authorize_user","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"detector_direct_authorization","outputs":[],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"detector","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_name","type":"string"},{"name":"_detector","type":"address"},{"name":"_latitude","type":"int256"},{"name":"_longitude","type":"int256"},{"name":"_seconds_allowed","type":"uint256"},{"name":"registry_address","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"}]"""
  }
}