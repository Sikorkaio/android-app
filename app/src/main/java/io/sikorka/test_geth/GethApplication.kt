package io.sikorka.test_geth

import android.app.Application
import io.sikorka.test_geth.di.modules.GethModule
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieApplicationModule

class GethApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    val scope = Toothpick.openScope(this)
    scope.installModules(SmoothieApplicationModule(this), GethModule())
  }
}
