package io.sikorka.android.ui.detector.bluetooth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.sikorka.android.R
import io.sikorka.android.helpers.fail
import io.sikorka.android.io.detectors.BtConnector
import io.sikorka.android.io.detectors.BtScanner
import io.sikorka.android.ui.contracts.deploydetectorcontract.DeployDetectorActivity
import io.sikorka.android.ui.dialogs.progress
import io.sikorka.android.ui.gone
import io.sikorka.android.ui.show
import io.sikorka.android.utils.isDisposed
import kotlinx.android.synthetic.main.activity_find_detector.*
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class FindBtDetectorActivity : AppCompatActivity(), FindBtDetectorView {

  private lateinit var scope: Scope

  @Inject
  lateinit var btScanner: BtScanner

  @Inject
  lateinit var btConnector: BtConnector

  @Inject
  lateinit var presenter: FindBtDetectorPresenter

  @Inject
  lateinit var detectorAdapter: FindDetectorAdapter

  private val compositeDisposable = CompositeDisposable()
  private var discoveryDisposable: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    Toothpick.openScope(PRESENTER_SCOPE).installModules(FindBtDetectorModule())
    scope = Toothpick.openScopes(application, PRESENTER_SCOPE, this)
    scope.installModules(SmoothieSupportActivityModule(this))
    super.onCreate(savedInstanceState)
    Toothpick.inject(this, scope)

    setContentView(R.layout.activity_find_detector)

    if (!btScanner.btSupport()) {

    }

    btScanner.enableBt(this, BT_ACTIVATE_REQUEST_CODE)
    find_detector__detector_list.apply {
      adapter = detectorAdapter
      layoutManager = LinearLayoutManager(this@FindBtDetectorActivity)
    }

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
      title = getString(R.string.find_detector__title)
    }

    discover()
    find_detector__swipe_layout.setOnRefreshListener { discover() }
  }

  override fun onDestroy() {
    Toothpick.closeScope(this)
    if (isFinishing) {
      Toothpick.closeScope(PRESENTER_SCOPE)
    }
    compositeDisposable.clear()
    super.onDestroy()
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)


    detectorAdapter.setOnClickListener {
      val dialog = progress(
          R.string.find_detector__connecting_title,
          R.string.find_detector__connecting_content
      )
      dialog.show()

      btConnector.connect(it)
          .flatMap { it.getDetectorEthAddress() }
          .subscribeOn(Schedulers.io())
          .doAfterTerminate {
            dialog.dismiss()
          }.subscribe({
        DeployDetectorActivity.start(this, it.hex, latitude, longitude)
      }) {
        Snackbar.make(find_detector__swipe_layout, R.string.find_detector__connection_failed, Snackbar.LENGTH_SHORT).show()
        Timber.e(it, "connection failed")
      }
    }
  }

  override fun onStop() {
    presenter.detach()
    detectorAdapter.setOnClickListener(null)
    super.onStop()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
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
          find_detector__loading_group.gone()
        }
        .subscribe({ devices ->
          detectorAdapter.update(devices)
          if (devices.isEmpty()) {
            find_detector__no_result_group.show()
          } else {
            find_detector__no_result_group.gone()
          }
        }) {
          Snackbar.make(find_detector__swipe_layout, R.string.find_detector__discovery_failed, Snackbar.LENGTH_SHORT).show()
          Timber.e(it, "error")
        }
  }


  private val latitude: Double
    get() = intent?.getDoubleExtra(LATITUDE, 0.0) ?: fail("got a null value instead")

  private val longitude: Double
    get() = intent?.getDoubleExtra(LONGITUDE, 0.0) ?: fail("got a null value instead")


  @javax.inject.Scope
  @Target(AnnotationTarget.TYPE)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class Presenter

  companion object {
    var PRESENTER_SCOPE: Class<*> = Presenter::class.java
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
