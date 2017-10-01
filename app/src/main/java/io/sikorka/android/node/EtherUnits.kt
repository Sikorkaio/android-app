package io.sikorka.android.node

import org.ethereum.geth.BigInt

private const val WEI = "wei"
private const val BABBAGE = "babbage"
private const val LOVELACE = "lovelace"
private const val SHANNON = "shannon"
private const val SZABO = "szabo"
private const val FINNEY = "finney"
private const val ETHER = "ether"

private var unitMap = mapOf(
    WEI to 1L,
    BABBAGE to 1000L,
    LOVELACE to 1000000L,
    SHANNON to 1000000000L,
    SZABO to 1000000000000L,
    FINNEY to 100000000000000L,
    ETHER to 1000000000000000000L
)

fun BigInt.toEther(): Double = toUnit(ETHER)

private fun BigInt.toUnit(unit: String): Double = this.int64.toDouble() / unitToValue(unit)

private fun unitToValue(unit: String): Long = unitMap.getOrDefault(unit, 1L)

fun etherToWei(ether: Double): Long = (ether * unitToValue(ETHER).toDouble()).toLong()

fun weiToEther(wei: Long): Double = (wei.toDouble() / unitToValue(ETHER))

