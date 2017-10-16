package io.sikorka.android.node.configuration

import java.io.File

fun prepareDataDir(dataDirectory: File): File {
  if (!dataDirectory.exists()) {
    dataDirectory.mkdir()
  }
  return dataDirectory
}