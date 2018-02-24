package io.sikorka.android.ui.detector.qr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import io.sikorka.android.R
import io.sikorka.android.ui.BaseActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrScannerActivity : BaseActivity(), ZXingScannerView.ResultHandler {
  private lateinit var scannerView: ZXingScannerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_simple_scanner)
    setupToolbar()

    val contentFrame = findViewById<FrameLayout>(R.id.content_frame) as ViewGroup
    scannerView = ZXingScannerView(this)
    scannerView.setFormats(listOf(BarcodeFormat.QR_CODE))
    contentFrame.addView(scannerView)

    if (!checkCameraPermissions()) {
      startCameraPermissionRequest()
    }

  }

  private fun startCameraPermissionRequest() {
    ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.CAMERA),
        REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE
    )
  }

  private fun checkCameraPermissions(): Boolean {
    val permissionState = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    )

    return permissionState == PackageManager.PERMISSION_GRANTED
  }


  override fun onResume() {
    super.onResume()
    scannerView.setResultHandler(this)
    scannerView.startCamera()
  }

  override fun onPause() {
    super.onPause()
    scannerView.stopCamera()
  }

  override fun handleResult(rawResult: Result) {
    val returnResult = Intent()
    returnResult.putExtra(DATA, rawResult.text)
    setResult(Activity.RESULT_OK, returnResult)
    finish()
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE) {

    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  companion object {
    const val REQUEST_CAMERA_PERMISSIONS_REQUEST_CODE = 1023
    const val SCANNER_RESULT = 1245

    const val DATA = "io.sikorka.android.extra.QR_DATA"

    fun start(context: Activity) {
      val intent = Intent(context, QrScannerActivity::class.java)
      context.startActivityForResult(intent, SCANNER_RESULT)
    }

  }
}
