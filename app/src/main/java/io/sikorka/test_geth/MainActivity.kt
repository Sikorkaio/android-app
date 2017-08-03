package io.sikorka.test_geth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import io.sikorka.test_geth.ui.accounts.AccountActivity


class MainActivity : AppCompatActivity() {

  @OnClick(R.id.main__accounts)
  internal fun openAccounts() {
    AccountActivity.start(this)
  }

  @OnClick(R.id.main__exit)
  internal fun onExit() {
    MyService.stop(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)
    val textbox = findViewById<TextView>(R.id.textbox)
    textbox.text = ""

    startService(Intent(this, MyService::class.java))

    val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
          MY_PERMISSIONS_REQUEST_STORAGE)

    }
  }

  companion object {
    const val MY_PERMISSIONS_REQUEST_STORAGE = 1245
  }
}
