package io.sikorka.android.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.sikorka.android.GethService
import io.sikorka.android.R
import io.sikorka.android.di.modules.SikorkaModule
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.ui.wizard.WizardActivity
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieApplicationModule
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

  @Inject internal lateinit var appPreferences: AppPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScope(application)
    scope.installModules(SmoothieApplicationModule(application), SikorkaModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__splash)
    Toothpick.inject(this, scope)

    Completable.timer(400, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          if (appPreferences.selectedAccount().isNotBlank()) {
            GethService.start(this)
            MainActivity.start(this)
          } else {
            WizardActivity.start(this)
          }

          finish()

        }
  }
}