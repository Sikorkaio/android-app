package io.sikorka.test_geth.node.configuration

import android.os.Environment
import java.io.File

fun prepareDataDir(dataDirectory: String): File {

  val externalDataDir = File(Environment.getExternalStorageDirectory(), "/$dataDirectory")
  if (!externalDataDir.exists()) {
    externalDataDir.mkdir()
  }

  return externalDataDir
}