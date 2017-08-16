package io.sikorka.android.di.modules

import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.PassphraseValidatorImpl
import io.sikorka.android.di.providers.KeystorePathProvider
import io.sikorka.android.di.qualifiers.KeystorePath
import toothpick.config.Module

class GethModule : Module() {
  init {
    bind(String::class.java).withName(KeystorePath::class.java).toProvider(KeystorePathProvider::class.java)
    bind(PassphraseValidator::class.java).to(PassphraseValidatorImpl::class.java)
  }
}