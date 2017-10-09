package io.sikorka.android.node

import org.ethereum.geth.BigInt
import java.math.BigDecimal
import java.math.RoundingMode

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

fun BigDecimal.toEther(): Double {
  val value = unitToValue(ETHER)
  val divisor = BigDecimal(value)
  val division = this.divide(divisor)
  return division.setScale(2, RoundingMode.DOWN).toDouble()
}

private fun BigInt.toUnit(unit: String): Double = this.int64.toDouble() / unitToValue(unit)

private fun unitToValue(unit: String): Long = unitMap[unit] ?: 1L

fun etherToWei(ether: Double): Long = (ether * unitToValue(ETHER).toDouble()).toLong()

fun weiToEther(wei: Long): Double = (wei.toDouble() / unitToValue(ETHER))

