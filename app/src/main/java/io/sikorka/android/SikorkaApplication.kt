package io.sikorka.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import io.sikorka.android.di.modules.sikorkaModule
import io.sikorka.android.ui.viewModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class SikorkaApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)

    startKoin(this, listOf(sikorkaModule, viewModule))

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