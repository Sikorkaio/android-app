package io.sikorka.android.core

import android.support.annotation.StringDef
import io.sikorka.android.core.EtherUnits.Currency
import io.sikorka.android.core.EtherUnits.ETHER
import io.sikorka.android.core.EtherUnits.FINNEY
import io.sikorka.android.core.EtherUnits.GWEI
import io.sikorka.android.core.EtherUnits.KWEI
import io.sikorka.android.core.EtherUnits.MWEI
import io.sikorka.android.core.EtherUnits.SZABO
import io.sikorka.android.core.EtherUnits.WEI
import org.ethereum.geth.BigInt
import java.math.BigDecimal
import java.math.RoundingMode

object EtherUnits {
  const val WEI = "wei"
  const val KWEI = "kwei"
  const val MWEI = "mwei"
  const val GWEI = "gwei"
  const val SZABO = "szabo"
  const val FINNEY = "finney"
  const val ETHER = "ether"

  val units = arrayOf(WEI,
    KWEI,
    MWEI,
    GWEI,
    SZABO,
    FINNEY,
    ETHER)

  @StringDef(
    WEI,
    KWEI,
    MWEI,
    GWEI,
    SZABO,
    FINNEY,
    ETHER
  )
  @Retention(AnnotationRetention.SOURCE)
  annotation class Currency
}

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
  return division.setScale(20, RoundingMode.DOWN).toDouble()
}

private fun BigInt.toUnit(@Currency unit: String): Double {
  return this.int64.toDouble() / unitToValue(unit)
}

private fun unitToValue(@Currency unit: String): Long = unitMap[unit] ?: 1L

fun valueToUnit(amount: Long, @Currency unit: String): Long {
  return amount / unitToValue(unit)
}

fun valueToWei(amount: Long, @Currency unit: String): Long {
  return amount * unitToValue(unit)
}

@Currency
fun findUnit(amount: Long): String {
  val keys = unitMap.keys
  val unit = keys.reversed().firstOrNull { isUnit(amount, it) }
  return unit ?: WEI
}

private fun isUnit(amount: Long, @Currency unit: String): Boolean {
  val remaining = amount % unitToValue(unit)
  val result = amount / unitToValue(unit)
  return remaining == 0L && result > 0
}