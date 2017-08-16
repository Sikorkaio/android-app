package io.sikorka.android.node.configuration

import android.content.res.AssetManager
import io.sikorka.android.io.copyToFile
import java.io.File
import javax.inject.Inject

class PeerHelper
@Inject
constructor(private val assetManager: AssetManager) {

  fun prepareStaticNodes(peerFile: String, assetFilename: String) {
    val file = File(peerFile)

    val inputStream = assetManager.open(assetFilename)
    if (file.exists()) {
      file.delete()
      file.createNewFile()
    } else {
      file.mkdirs()
    }

    inputStream.copyToFile(file)
  }
}