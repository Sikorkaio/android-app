package io.sikorka.android

import android.app.Application
import io.sikorka.android.di.modules.GethModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator
import toothpick.smoothie.module.SmoothieApplicationModule

class GethApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    val scope = Toothpick.openScope(this)
    scope.installModules(SmoothieApplicationModule(this), GethModule())

    if (BuildConfig.DEBUG) {
      Toothpick.setConfiguration(Configuration.forDevelopment().disableReflection())
    } else {
      Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
    }

    FactoryRegistryLocator.setRootRegistry(io.sikorka.android.di.FactoryRegistry())
    MemberInjectorRegistryLocator.setRootRegistry(io.sikorka.android.di.MemberInjectorRegistry())
  }
}
