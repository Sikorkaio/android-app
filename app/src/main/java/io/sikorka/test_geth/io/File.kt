package io.sikorka.test_geth.io

import okio.Okio
import java.io.File
import java.io.InputStream


fun File.copyToDirectory(targetLocation: File) {

  if (isDirectory) {
    if (!targetLocation.exists()) {
      targetLocation.mkdirs()
    }

    val children = list()
    for (i in children.indices) {
      File(this@copyToDirectory, children[i]).copyTo(File(targetLocation, children[i]))
    }
  } else {
    this.copyToFile(targetLocation)
  }
}

/**
 * @param this@copyFile
 * @param targetLocation
 */

fun File.copyToFile(targetLocation: File) {
  val sink = Okio.sink(targetLocation)
  val source = Okio.source(this)
  val bufferedSource = Okio.buffer(source)
  val bufferedSink = Okio.buffer(sink)
  bufferedSink.writeAll(bufferedSource)
  bufferedSource.close()
  bufferedSink.flush()
  bufferedSink.close()
}

fun File.deleteDirectory(): Boolean {
  if (exists()) {
    val files = listFiles()
    for (i in files.indices) {
      if (files[i].isDirectory) {
        files[i].deleteDirectory()
      } else {
        files[i].delete()
      }
    }
  }
  return delete()
}

fun InputStream.copyToFile(file: File) {
  val sink = Okio.sink(file)
  val source = Okio.source(this)
  val bufferedSource = Okio.buffer(source)
  val bufferedSink = Okio.buffer(sink)
  bufferedSink.writeAll(bufferedSource)
  bufferedSource.close()
  bufferedSink.flush()
  bufferedSink.close()
  this.close()
}