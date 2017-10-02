package io.sikorka.android.helpers.sha3

import io.sikorka.android.helpers.sha3.HexUtils.getHex
import io.sikorka.android.helpers.sha3.Parameters.KECCAK_256

private val keccak = Keccak()

fun String.kekkac256(): String = keccak.getHash(getHex(toByteArray()), KECCAK_256)
