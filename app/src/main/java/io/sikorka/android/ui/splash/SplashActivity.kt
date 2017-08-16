package io.sikorka.android.ui.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.sikorka.android.R
import io.sikorka.android.ui.wizard.WizardActivity
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__splash)

    Completable.timer(400, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          WizardActivity.start(this)
        }
  }
}