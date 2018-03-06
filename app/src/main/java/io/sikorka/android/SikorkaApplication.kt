package io.sikorka.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import io.sikorka.android.di.modules.SikorkaModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator
import toothpick.smoothie.module.SmoothieApplicationModule

class SikorkaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    val scope = Toothpick.openScope(this)
    scope.installModules(SmoothieApplicationModule(this), SikorkaModule())

    if (BuildConfig.DEBUG) {
      Toothpick.setConfiguration(Configuration.forDevelopment().disableReflection())
    } else {
      Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
    }

    FactoryRegistryLocator.setRootRegistry(io.sikorka.android.di.FactoryRegistry())
    MemberInjectorRegistryLocator.setRootRegistry(io.sikorka.android.di.MemberInjectorRegistry())

    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
          return "${super.createStackElementTag(element)}:" +
            "${element.lineNumber} [${Thread.currentThread().name}]"
        }
      })
      LeakCanary.install(this)
    }
  }
}