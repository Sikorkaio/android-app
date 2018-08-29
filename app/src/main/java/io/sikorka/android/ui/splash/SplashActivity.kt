package io.sikorka.android.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.sikorka.android.R
import io.sikorka.android.SikorkaService
import io.sikorka.android.settings.AppPreferences
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.ui.wizard.WizardActivity
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

  private val appPreferences: AppPreferences by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__splash)

    launch {
      delay(400)

      if (appPreferences.selectedAccount().isNotBlank()) {
        SikorkaService.start(this@SplashActivity)
        MainActivity.start(this@SplashActivity)
      } else {
        WizardActivity.start(this@SplashActivity)
      }

      finish()
    }
  }
}