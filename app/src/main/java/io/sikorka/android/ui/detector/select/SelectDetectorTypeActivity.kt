package io.sikorka.android.ui.detector.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.detector.bluetooth.FindBtDetectorActivity
import kotlinx.android.synthetic.main.activity_select_detector_type.*

class SelectDetectorTypeActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_detector_type)
    supportActionBar?.apply {
      setHomeButtonEnabled(true)
      setDisplayHomeAsUpEnabled(true)
      title = getString(R.string.select_detector_type__title)
    }
    val detectorTypeAdapter = SelectDetectorTypeAdapter(this@SelectDetectorTypeActivity, detectors())
    detectorTypeAdapter.setOnSelection { typeId ->
      when(typeId) {
        SupportedDetectors.BLUETOOTH -> FindBtDetectorActivity.start(this, latitude, longitude)
      }
    }
    select_detector_type__supported_detectors.apply {
      adapter = detectorTypeAdapter
      layoutManager = LinearLayoutManager(this@SelectDetectorTypeActivity)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  private fun detectors(): List<SupportedDetector> {
    val detectors = ArrayList<SupportedDetector>()
    detectors.add(SupportedDetector(
        SupportedDetectors.BLUETOOTH,
        R.string.select_detector_type__detector_bluetooth,
        R.drawable.ic_bluetooth_black_24dp
    ))
    return detectors
  }

  private val latitude: Double
    get() = intent?.getDoubleExtra(LATITUDE, 0.0) ?: fail("got a null value instead")

  private val longitude: Double
    get() = intent?.getDoubleExtra(LONGITUDE, 0.0) ?: fail("got a null value instead")


  companion object {
    private const val LATITUDE = "io.sikorka.android.extras.LATITUDE"
    private const val LONGITUDE = "io.sikorka.android.extras.LONGITUDE"

    fun start(context: Context, latitude: Double, longitude: Double) {
      val intent = Intent(context, SelectDetectorTypeActivity::class.java)
      intent.putExtra(LATITUDE, latitude)
      intent.putExtra(LONGITUDE, longitude)
      context.startActivity(intent)
    }
  }
}
