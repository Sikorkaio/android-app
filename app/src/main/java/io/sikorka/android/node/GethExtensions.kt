package io.sikorka.android.node

import org.ethereum.geth.Address
import org.ethereum.geth.Addresses
import org.ethereum.geth.BigInt
import org.ethereum.geth.BigInts

fun Addresses.all() = object : Iterable<Address> {
  override fun iterator() = object : Iterator<Address> {
    private var current = 0L

    override fun hasNext(): Boolean = current < size()

    override fun next(): Address = get(current++)
  }
}


fun BigInts.values() = object : Iterable<BigInt> {
  override fun iterator() = object : Iterator<BigInt> {
    private var current = 0L

    override fun hasNext(): Boolean = current < size()

    override fun next(): BigInt = get(current++)
  }

}