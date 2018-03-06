package io.sikorka.android.io

import okio.Okio
import java.io.ByteArrayInputStream
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

fun ByteArray.toFile(file: File) {
  val sink = Okio.sink(file)
  val source = Okio.source(ByteArrayInputStream(this))
  val bufferedSource = Okio.buffer(source)
  val bufferedSink = Okio.buffer(sink)
  bufferedSink.writeAll(bufferedSource)
  bufferedSource.close()
  bufferedSink.flush()
  bufferedSink.close()
}

fun File.toByteArray(): ByteArray {
  val source = Okio.source(this)
  val bufferedSource = Okio.buffer(source)
  val data = bufferedSource.readByteArray()
  bufferedSource.close()
  source.close()
  return data
}

fun bytes(bytes: Long, si: Boolean = false): String {
  val unit = if (si) 1000 else 1024
  if (bytes < unit) return bytes.toString() + " B"
  val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
  val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
  return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
}