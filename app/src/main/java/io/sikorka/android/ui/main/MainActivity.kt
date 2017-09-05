package io.sikorka.android.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.sikorka.android.GethService
import io.sikorka.android.R
import io.sikorka.android.node.accounts.AccountModel
import io.sikorka.android.ui.accounts.AccountActivity
import kotlinx.android.synthetic.main.activity__main.*
import kotlinx.android.synthetic.main.app_bar__main.*
import kotlinx.android.synthetic.main.nav_header__main.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import javax.inject.Inject


class MainActivity : AppCompatActivity(),
    MainView,
    OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

  @Inject internal lateinit var presenter: MainPresenter

  private lateinit var scope: Scope
  private var map: GoogleMap? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    scope = Toothpick.openScopes(application, this)
    scope.installModules(SmoothieSupportActivityModule(this), MainModule())
    Toothpick.inject(this, scope)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity__main)
    val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)

    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
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
  }


  private fun getLocation() {
    val client = LocationServices.getFusedLocationProviderClient(this)
    client.lastLocation.addOnCompleteListener(this, {
      if (!it.isSuccessful) {
        return@addOnCompleteListener
      }
      val map = map ?: return@addOnCompleteListener
      val location = it.result ?: return@addOnCompleteListener
      updateMyMarker(location, map)
    })
  }

  override fun updateAccountInfo(model: AccountModel) {
    main__header_account.text = model.account
    main__header_balance.text = if (model.ethBalance < 0) {
      getString(R.string.main__no_balance_available)
    } else {
      getString(R.string.main_balance_eth, model.ethBalance)
    }
  }

  private fun updateMyMarker(location: Location, map: GoogleMap) {
    val me = LatLng(location.latitude, location.longitude)

    val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp)

    map.moveCamera(CameraUpdateFactory.newLatLng(me))
    map.addMarker(MarkerOptions().position(me)
        .title("Me"))
  }

  private fun startLocationPermissionRequest() {
    ActivityCompat.requestPermissions(this@MainActivity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_PERMISSIONS_REQUEST_CODE)
  }

  private fun checkPermissions(): Boolean {
    val permissionState = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    return permissionState == PackageManager.PERMISSION_GRANTED
  }


  override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }


  override fun onDestroy() {
    super.onDestroy()
    Toothpick.reset()
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
    presenter.loadAccountInfo()
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
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // Handle navigation view item clicks here.
    when (item.itemId) {
      R.id.main__nav_accounts -> {
        AccountActivity.start(this)
      }
      R.id.main__nav_exit -> {
        GethService.stop(this)
        finish()
      }
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    map = googleMap

    if (checkPermissions()) {
      getLocation()
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
