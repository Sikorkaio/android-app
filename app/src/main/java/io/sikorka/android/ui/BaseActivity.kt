package io.sikorka.android.ui

import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
  fun setupToolbar() {
    val ab = supportActionBar
    ab?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
    // Respond to the action bar's Up/Home button
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  protected fun snackBar(@StringRes resId: Int) {
    Snackbar.make(findViewById(android.R.id.content), resId, Snackbar.LENGTH_SHORT).show()
  }
}