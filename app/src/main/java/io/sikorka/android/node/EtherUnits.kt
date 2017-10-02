package io.sikorka.android.node

import org.ethereum.geth.BigInt

private const val WEI = "wei"
private const val KWEI = "kwei"
private const val MWEI = "mwei"
private const val GWEI = "gwei"
private const val SZABO = "szabo"
private const val FINNEY = "finney"
private const val ETHER = "ether"

private var unitMap = mapOf(
    WEI to 1L,
    KWEI to 1000L,
    MWEI to 1000000L,
    GWEI to 1000000000L,
    SZABO to 1000000000000L,
    FINNEY to 100000000000000L,
    ETHER to 1000000000000000000L
)

fun BigInt.toEther(): Double = toUnit(ETHER)

private fun BigInt.toUnit(unit: String): Double = this.int64.toDouble() / unitToValue(unit)

private fun unitToValue(unit: String): Long = unitMap.getOrDefault(unit, 1L)

fun etherToWei(ether: Double): Long = (ether * unitToValue(ETHER).toDouble()).toLong()

fun weiToEther(wei: Long): Double = (wei.toDouble() / unitToValue(ETHER))

