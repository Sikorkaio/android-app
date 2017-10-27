package io.sikorka.android.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.sikorka.android.BuildConfig
import io.sikorka.android.GethService
import io.sikorka.android.R
import io.sikorka.android.node.SyncStatus
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.node.contracts.DeployedContractModel
import io.sikorka.android.ui.MenuTint
import io.sikorka.android.ui.accounts.AccountActivity
import io.sikorka.android.ui.contracts.DeployContractActivity
import io.sikorka.android.ui.contracts.interact.ContractInteractActivity
import io.sikorka.android.ui.contracts.pending.PendingContractsActivity
import io.sikorka.android.ui.detector.select.SelectDetectorTypeActivity
import io.sikorka.android.ui.dialogs.showConfirmation
import io.sikorka.android.ui.dialogs.useDetector
import io.sikorka.android.ui.progressSnack
import io.sikorka.android.ui.settings.DebugPreferencesStore
import io.sikorka.android.ui.settings.SettingsActivity
import io.sikorka.android.ui.show
import io.sikorka.android.utils.getBitmapFromVectorDrawable
import kotlinx.android.synthetic.main.activity__main.*
import kotlinx.android.synthetic.main.app_bar__main.*
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(),
    MainView,
    OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

  @Inject internal lateinit var presenter: MainPresenter
  @Inject internal lateinit var debugPreferences: DebugPreferencesStore

  private lateinit var scope: Scope
  private var map: GoogleMap? = null

  private var myMarker: Marker? = null

  private var latitude: Double = 0.0
  private var longitude: Double = 0.0

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), MainModule())
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__main)
    Toothpick.inject(this, scope)
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    setSupportActionBar(toolbar)

    main__deploy_fab.setOnClickListener {
      useDetector { use ->
        if (use) {
          SelectDetectorTypeActivity.start(this, latitude, longitude)
        } else {
          DeployContractActivity.start(this, latitude, longitude)
        }
      }.show()
    }

    val toggle = ActionBarDrawerToggle(
        this,
        drawer_layout,
        toolbar,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    )
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    main__nav_view.setNavigationItemSelectedListener(this)

    if (!checkPermissions()) {
      startLocationPermissionRequest()
    }

    if (!checkWritePermissions()) {
      Timber.v("Didn't have storage write permissions")
      startWritePermissionRequest()
    }

    main__nav_exit.setOnClickListener {
      GethService.stop(this)
      finish()
      drawer_layout.closeDrawer(GravityCompat.START)
    }

    main__nav_network_statistics.setText(R.string.main__no_peers_available)

    if (BuildConfig.DEBUG) {
      main__nav_debug_randomize.show()
      main__nav_debug_randomize.isChecked = debugPreferences.isLocationRandomizationEnabled()
      main__nav_debug_randomize.setOnCheckedChangeListener { _, enabled ->
        debugPreferences.setLocationRandomiztion(enabled)
        getLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  private fun getLocation() {
    LocationServices.getFusedLocationProviderClient(this)
        .lastLocation
        .addOnCompleteListener(this, {
          if (!it.isSuccessful) {
            return@addOnCompleteListener
          }
          val map = map ?: return@addOnCompleteListener
          val location = it.result ?: return@addOnCompleteListener

          if (BuildConfig.DEBUG && debugPreferences.isLocationRandomizationEnabled()) {
            val random = Random()
            val longAdd = random.nextBoolean()
            val latAdd = random.nextBoolean()
            val latAdj = random.nextInt(10)
            val longAdj = random.nextInt(10)

            fun Double.randomize(boolean: Boolean, adjust: Int): Double {
              val extra = adjust / 100.0
              return if (boolean) this + extra else this - extra
            }

            longitude = location.longitude.randomize(longAdd, longAdj)
            latitude = location.latitude.randomize(latAdd, latAdj)
          } else {
            longitude = location.longitude
            latitude = location.latitude
          }

          updateMyMarker(latitude, longitude, map)
        })
  }

  override fun updateSyncStatus(status: SyncStatus) {
    val statusMessage = getString(
        R.string.main_nav__network_statistics,
        status.peers,
        status.currentBlock,
        status.highestBlock
    )
    main__nav_network_statistics.text = statusMessage
  }

  override fun updateAccountInfo(model: AccountModel) {
    val view = main__nav_view.getHeaderView(0)
    view.findViewById<TextView>(R.id.main__header_account).text = model.addressHex
    view.findViewById<TextView>(R.id.main__header_balance).text = if (model.ethBalance < 0) {
      getString(R.string.main_nav__header_no_balance)
    } else {
      getString(R.string.main_nav_balance_eth, model.ethBalance)
    }
  }

  private fun updateMyMarker(latitude: Double, longitude: Double, map: GoogleMap) {
    myMarker?.remove()

    val me = LatLng(latitude, longitude)

    val bitmap = getBitmapFromVectorDrawable(R.drawable.ic_person_pin_circle_black_24dp)
    val icon = BitmapDescriptorFactory.fromBitmap(bitmap)

    val position = CameraPosition.builder()
        .target(me)
        .zoom(16f)
        .bearing(0.0f)
        .tilt(0.0f)
        .build()

    map.run {
      animateCamera(CameraUpdateFactory.newCameraPosition(position), null)
      moveCamera(CameraUpdateFactory.newCameraPosition(position))
      myMarker = addMarker(MarkerOptions().position(me).title("Me").icon(icon))
    }
  }


  private fun startLocationPermissionRequest() {
    ActivityCompat.requestPermissions(this@MainActivity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_PERMISSIONS_REQUEST_CODE)
  }

  private fun startWritePermissionRequest() {
    ActivityCompat.requestPermissions(this@MainActivity,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        REQUEST_PERMISSIONS_REQUEST_CODE)
  }

  private fun checkPermissions(): Boolean {
    val permissionState = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    return permissionState == PackageManager.PERMISSION_GRANTED
  }

  private fun checkWritePermissions(): Boolean {
    val permissionState = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    return permissionState == PackageManager.PERMISSION_GRANTED
  }


  override fun onDestroy() {
    super.onDestroy()
    Toothpick.reset()
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.load()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    MenuTint.on(menu).setMenuItemIconColor(ContextCompat.getColor(this, R.color.white)).apply(this)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> {
        SettingsActivity.start(this)
        true
      }
      R.id.action_reload -> {
        presenter.load()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.main__nav_accounts -> {
        AccountActivity.start(this)
      }
      R.id.main__nav_pending_contracts -> {
        PendingContractsActivity.start(this)
      }
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    map = googleMap

    map?.setOnInfoWindowClickListener { marker ->
      val contractAddress = marker.tag as String? ?: return@setOnInfoWindowClickListener

      showConfirmation(
          R.string.main__contract_intration_dialog_title,
          R.string.main__contract_intration_dialog_content,
          contractAddress
      ) {
        ContractInteractActivity.start(this, contractAddress)
      }

    }

    if (checkPermissions()) {
      getLocation()
    }
  }

  override fun error(error: Throwable) {
    val message = error.message ?: getString(R.string.errors__generic_error)
    Snackbar.make(main__deploy_fab, message, Snackbar.LENGTH_SHORT).show()
  }

  private var progress: Snackbar? = null

  override fun loading(loading: Boolean) {
    if (loading) {
      main__deploy_fab.hide()
      progress = main__deploy_fab.progressSnack(R.string.main__loading_message, Snackbar.LENGTH_INDEFINITE)
      progress?.show()
    } else {
      main__deploy_fab.show()
      progress?.dismiss()
    }
  }

  override fun update(model: DeployedContractModel) {
    val googleMap = map ?: return

    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_ethereum_icon)
    val icon = BitmapDescriptorFactory.fromBitmap(bitmap)

    model.data.forEach {
      val markerOptions = MarkerOptions()
          .position(LatLng(it.latitude, it.longitude))
          .title(it.addressHex)
          .icon(icon)

      val marker = googleMap.addMarker(markerOptions)
      marker.tag = it.addressHex
    }
  }




  companion object {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    fun start(context: Context) {
      val intent = Intent(context, MainActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }
}
