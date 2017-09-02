package io.sikorka.android.ui.wizard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import io.sikorka.android.GethService
import io.sikorka.android.ui.main.MainActivity
import io.sikorka.android.ui.wizard.slides.InformationFragment
import io.sikorka.android.ui.wizard.slides.account_setup.AccountSetupFragment
import io.sikorka.android.ui.wizard.slides.network_selection.NetworkSelectionFragment

class WizardActivity : AppIntro2() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    askForPermissions(arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ), 2)


    addSlide(InformationFragment.newInstance())
    addSlide(NetworkSelectionFragment.newInstance())
    addSlide(AccountSetupFragment.newInstance())
    progressButtonEnabled = true
    setNextPageSwipeLock(false)
    setSwipeLock(false)
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    GethService.start(this)
    MainActivity.start(this)
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, WizardActivity::class.java)
      context.startActivity(intent)
    }
  }
}