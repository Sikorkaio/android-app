package io.sikorka.test_geth.di.modules

import io.sikorka.test_geth.di.providers.KeystorePathProvider
import io.sikorka.test_geth.di.qualifiers.KeystorePath
import toothpick.config.Module

class GethModule : Module() {
  init {
    bind(String::class.java).withName(KeystorePath::class.java).toProvider(KeystorePathProvider::class.java)
  }
}