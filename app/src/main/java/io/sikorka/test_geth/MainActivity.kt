package io.sikorka.test_geth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val textbox = findViewById(R.id.textbox) as TextView
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
