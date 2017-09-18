package io.sikorka.android.di.modules

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.sikorka.android.di.providers.KeystorePathProvider
import io.sikorka.android.di.qualifiers.IoScheduler
import io.sikorka.android.di.qualifiers.KeystorePath
import io.sikorka.android.di.qualifiers.MainScheduler
import io.sikorka.android.events.RxBus
import io.sikorka.android.events.RxBusImpl
import io.sikorka.android.node.accounts.PassphraseValidator
import io.sikorka.android.node.accounts.PassphraseValidatorImpl
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.settings.AppPreferencesImpl
import toothpick.config.Module

class GethModule : Module() {
  init {
    bind(String::class.java).withName(KeystorePath::class.java).toProvider(KeystorePathProvider::class.java)
    bind(Scheduler::class.java).withName(IoScheduler::class.java).toProviderInstance { Schedulers.io() }
    bind(Scheduler::class.java).withName(MainScheduler::class.java).toProviderInstance { AndroidSchedulers.mainThread() }
    bind(PassphraseValidator::class.java).to(PassphraseValidatorImpl::class.java)
    bind(AppPreferences::class.java).to(AppPreferencesImpl::class.java)
    bind(RxBus::class.java).to(RxBusImpl::class.java).singletonInScope()
  }
}