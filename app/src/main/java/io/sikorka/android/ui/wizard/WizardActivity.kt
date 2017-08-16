package io.sikorka.android.ui.wizard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro2
import io.sikorka.android.ui.wizard.slides.AccountManagementFragment
import io.sikorka.android.ui.wizard.slides.InformationFragment
import io.sikorka.android.ui.wizard.slides.NetworkSelectionFragment

class WizardActivity : AppIntro2() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    askForPermissions(arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ), 2)


    addSlide(InformationFragment.newInstance())
    addSlide(NetworkSelectionFragment.newInstance())
    addSlide(AccountManagementFragment.newInstance())
  }

  companion object {
    fun start(context: Context) {
      val intent = Intent(context, WizardActivity::class.java)
      context.startActivity(intent)
    }
  }
}