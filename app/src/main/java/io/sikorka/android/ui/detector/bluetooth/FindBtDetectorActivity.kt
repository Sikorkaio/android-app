package io.sikorka.android.ui.detector.bluetooth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.io.detectors.BtConnector
import io.sikorka.android.io.detectors.BtScanner
import io.sikorka.android.ui.BaseActivity
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorActivity
import io.sikorka.android.ui.dialogs.progress
import io.sikorka.android.utils.isDisposed
import kotlinx.android.synthetic.main.activity_find_detector.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FindBtDetectorActivity : BaseActivity(), FindBtDetectorView {

  private val btScanner: BtScanner by inject()

  private val btConnector: BtConnector by inject()

  private val presenter: FindBtDetectorPresenter by inject()

  private val detectorAdapter: FindDetectorAdapter by inject()

  private val compositeDisposable = CompositeDisposable()
  private var discoveryDisposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_find_detector)

    if (!btScanner.btSupport()) {
    }

    btScanner.enableBt(this, BT_ACTIVATE_REQUEST_CODE)
    find_detector__detector_list.apply {
      adapter = detectorAdapter
      layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@FindBtDetectorActivity)
    }

    setupToolbar(R.string.find_detector__title)

    discover()
    find_detector__swipe_layout.setOnRefreshListener { discover() }
    presenter.attach(this)

    detectorAdapter.setOnClickListener { device ->
      val dialog = progress(
        R.string.find_detector__connecting_title,
        R.string.find_detector__connecting_content
      )
      dialog.show()

      btConnector.connect(device)
        .flatMap { it.getDetectorEthAddress() }
        .subscribeOn(Schedulers.io())
        .doAfterTerminate {
          dialog.dismiss()
        }.subscribe({
          DeployDetectorActivity.start(this, it.hex, latitude, longitude)
        }) {
          com.google.android.material.snackbar.Snackbar.make(
            find_detector__swipe_layout,
            R.string.find_detector__connection_failed,
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
          ).show()
          Timber.e(it, "connection failed")
        }
    }
  }

  override fun onDestroy() {
    detectorAdapter.setOnClickListener(null)
    presenter.detach()
    compositeDisposable.clear()
    super.onDestroy()
  }

  private fun discover() {
    if (!discoveryDisposable.isDisposed()) {
      return
    }

    discoveryDisposable = btScanner.discover(this)
      .buffer(15, TimeUnit.SECONDS)
      .distinct()
      .first(emptyList())
      .observeOn(AndroidSchedulers.mainThread())
      .doAfterTerminate {
        find_detector__swipe_layout.isRefreshing = false
        find_detector__loading_group.isVisible = false
      }
      .subscribe({ devices ->
        detectorAdapter.update(devices)
        find_detector__no_result_group.isVisible = devices.isEmpty()
      }) {
        com.google.android.material.snackbar.Snackbar.make(
          find_detector__swipe_layout,
          R.string.find_detector__discovery_failed,
          com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
        Timber.e(it, "error")
      }
  }

  private val latitude: Double
    get() = intent?.getDoubleExtra(LATITUDE, 0.0) ?: fail("got a null value instead")

  private val longitude: Double
    get() = intent?.getDoubleExtra(LONGITUDE, 0.0) ?: fail("got a null value instead")

  companion object {

    const val BT_ACTIVATE_REQUEST_CODE = 198
    private const val LATITUDE = "io.sikorka.android.extras.LATITUDE"
    private const val LONGITUDE = "io.sikorka.android.extras.LONGITUDE"

    fun start(context: Context, latitude: Double, longitude: Double) {
      val intent = Intent(context, FindBtDetectorActivity::class.java)
      intent.putExtra(LATITUDE, latitude)
      intent.putExtra(LONGITUDE, longitude)
      context.startActivity(intent)
    }
  }
}