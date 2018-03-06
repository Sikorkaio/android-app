package io.sikorka.android.ui.detector.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.sikorka.android.R
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorActivity
import io.sikorka.android.ui.detector.bluetooth.FindBtDetectorActivity
import kotterknife.bindView

class SelectDetectorTypeActivity : BaseActivity() {

  private val detectors: RecyclerView by bindView(R.id.select_detector_type__supported_detectors)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_detector_type)
    setupToolbar(R.string.select_detector_type__title)

    val detectorTypeAdapter = SelectDetectorTypeAdapter(
      this@SelectDetectorTypeActivity,
      detectors()
    )
    detectorTypeAdapter.setOnSelection { typeId ->
      when (typeId) {
        SupportedDetectors.BLUETOOTH -> {
          FindBtDetectorActivity.start(this, latitude, longitude)
        }
        SupportedDetectors.MANUAL -> {
          addressSpecificationDialog { address ->
            DeployDetectorActivity.start(this, address.hex, latitude, longitude)
          }
        }
      }
    }

    detectors.apply {
      adapter = detectorTypeAdapter
      layoutManager = LinearLayoutManager(this@SelectDetectorTypeActivity)
    }
  }

  private fun detectors(): List<SupportedDetector> {
    val detectors = ArrayList<SupportedDetector>()
    detectors.add(SupportedDetector(
      SupportedDetectors.MANUAL,
      R.string.select_detector_type__detector_manual,
      R.drawable.ic_edit_black_24dp
    ))
    detectors.add(SupportedDetector(
      SupportedDetectors.BLUETOOTH,
      R.string.select_detector_type__detector_bluetooth,
      R.drawable.ic_bluetooth_black_24dp
    ))
    return detectors
  }

  private val latitude: Double
    get() = checkNotNull(intent?.getDoubleExtra(LATITUDE, 0.0)) {
      "got a null value instead"
    }

  private val longitude: Double
    get() = checkNotNull(intent?.getDoubleExtra(LONGITUDE, 0.0)) {
      "got a null value instead"
    }

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