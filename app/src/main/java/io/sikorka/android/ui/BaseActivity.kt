package io.sikorka.android.ui

import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import io.sikorka.android.R
import kotterknife.bindView

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
  protected val toolbar: Toolbar by bindView(R.id.toolbar)

  fun setupToolbar(@StringRes resId: Int = -1, title: String = "") {
    setSupportActionBar(toolbar)

    val ab = supportActionBar
    ab?.setDisplayHomeAsUpEnabled(true)

    if (resId != -1) {
      ab?.setTitle(resId)
    }

    if (title.isNotBlank()) {
      ab?.title = title
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
    // Respond to the action bar's Up/Home button
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  protected fun snackBar(@StringRes resId: Int) {
    Snackbar.make(findViewById(android.R.id.content), resId, Snackbar.LENGTH_SHORT).show()
  }
}