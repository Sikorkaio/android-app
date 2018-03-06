package io.sikorka.android.di.providers

import android.app.Application
import javax.inject.Inject
import javax.inject.Provider

class KeystorePathProvider
@Inject constructor(private val application: Application) : Provider<String> {
  override fun get(): String = "${application.filesDir}/keystore"
}