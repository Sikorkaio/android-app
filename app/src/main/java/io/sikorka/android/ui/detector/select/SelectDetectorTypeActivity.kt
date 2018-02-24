package io.sikorka.android.ui.detector.select

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorActivity
import io.sikorka.android.ui.detector.bluetooth.FindBtDetectorActivity
import kotlinx.android.synthetic.main.activity_select_detector_type.*
import org.ethereum.geth.Address
import org.ethereum.geth.Geth

class SelectDetectorTypeActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_detector_type)
    setupToolbar(R.string.select_detector_type__title)

    val detectorTypeAdapter = SelectDetectorTypeAdapter(this@SelectDetectorTypeActivity, detectors())
    detectorTypeAdapter.setOnSelection { typeId ->
      when (typeId) {
        SupportedDetectors.BLUETOOTH -> FindBtDetectorActivity.start(this, latitude, longitude)
        SupportedDetectors.MANUAL -> {
          specify {
            DeployDetectorActivity.start(this, it.hex, latitude, longitude)
          }
        }
      }
    }
    select_detector_type__supported_detectors.apply {
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

  private fun specify(onAddress: (address: Address) -> Unit) {
    MaterialDialog.Builder(this)
        .title(R.string.select_detector__manual_dialog_title)
        .titleColorRes(R.color.colorAccent)
        .content(R.string.select_detector__manual_dialog_content)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .autoDismiss(false)
        .negativeText(android.R.string.cancel)
        .onNegative { dialog, _ -> dialog.dismiss() }
        .input(R.string.select_detector__manual_dialog_hint, R.string.select_detector__manual_dialog_prefill) { dialog, input ->

          val address = if (input.startsWith("0x")) {
            input.toString().replace("0x", "")
          } else {
            input.toString()
          }

          val addr = try {
            Geth.newAddressFromHex(address)
          } catch (ex: Exception) {
            null
          }

          if (addr == null) {
            Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show()
          } else {
            onAddress(addr)
          }

        }.show()
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
