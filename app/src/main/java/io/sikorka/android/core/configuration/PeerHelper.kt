package io.sikorka.android.core.configuration

import android.content.res.AssetManager
import io.sikorka.android.io.copyToFile
import java.io.File

class PeerHelper(private val assetManager: AssetManager) {

  fun prepareStaticNodes(peerFile: String, assetFilename: String) {
    val file = File(peerFile)

    if (file.exists()) {
      return
    } else {
      val parentFile = file.parentFile
      if (!parentFile.exists()) {
        parentFile.mkdirs()
      }
      file.createNewFile()
    }

    assetManager.open(assetFilename).copyToFile(file)
  }
}