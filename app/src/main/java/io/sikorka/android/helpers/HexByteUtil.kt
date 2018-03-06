package io.sikorka.android.helpers

private val HEX_CHARS = "0123456789abcdef".toCharArray()

fun ByteArray.toHex(): String {
  val result = StringBuffer().append("0x")

  forEach {
    val octet = it.toInt()
    val firstIndex = (octet and 0xF0).ushr(4)
    val secondIndex = octet and 0x0F
    result.append(HEX_CHARS[firstIndex])
    result.append(HEX_CHARS[secondIndex])
  }

  return result.toString()
}

private val STRING_HEX_CHARS = "0123456789abcdef"

fun String.hexStringToByteArray(): ByteArray {

  val hex = if (startsWith("0x", true)) {
    this.removePrefix("0x")
  } else {
    this
  }

  val result = ByteArray(hex.length / 2)

  for (i in 0 until hex.length step 2) {
    val firstIndex = STRING_HEX_CHARS.indexOf(hex[i])
    val secondIndex = STRING_HEX_CHARS.indexOf(hex[i + 1])

    val octet = firstIndex.shl(4).or(secondIndex)
    result[i.shr(1)] = octet.toByte()
  }

  return result
}