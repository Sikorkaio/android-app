package io.sikorka.android.core.configuration

import java.io.File

fun prepareDataDir(dataDirectory: File): File {
  if (!dataDirectory.exists()) {
    dataDirectory.mkdir()
  }
  return dataDirectory
}