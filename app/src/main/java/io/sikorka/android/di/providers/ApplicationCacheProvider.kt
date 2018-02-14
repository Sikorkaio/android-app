package io.sikorka.android.di.providers

import android.app.Application
import java.io.File
import javax.inject.Inject
import javax.inject.Provider

class ApplicationCacheProvider
@Inject
constructor(private val application: Application) : Provider<File> {
  override fun get(): File = application.externalCacheDir ?: application.cacheDir
}