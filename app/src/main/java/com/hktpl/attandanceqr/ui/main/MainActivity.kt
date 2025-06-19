package com.hktpl.attandanceqr.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityMainBinding
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.ui.history.HistoryActivity
import com.hktpl.attandanceqr.ui.profile.ProfileActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : BaseActivity() , OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: UserPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var staticLocation = LatLng(26.8566907, 75.8287823)
    private var siteOid = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolBar.txtToolbarTitle.text = getString(R.string.home)
        preferences = UserPreferences(this)
        binding.userData = preferences
        binding.txtAppVersion.text = appVersion
        binding.txtClock.text = Calendar.DATE.toString()
        binding.imgMenu.setOnClickListener {
            val popupMenu = PopupMenu(it.context,binding.imgMenu)
            popupMenu.menu.add("My Profile")
                .setOnMenuItemClickListener {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
            popupMenu.menu.add("History")
                .setOnMenuItemClickListener {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
            popupMenu.show()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        binding.fabButton.setOnClickListener {
            val scanOptions = ScanOptions()
            scanOptions.setOrientationLocked(false)
            scanOptions.setTorchEnabled(false)
            scanOptions.setTimeout(10000)
            scanOptions.setPrompt("Scan QR Code")
            barcodeLauncher.launch(scanOptions)
        }
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
//this code is for
        if (result.contents == null) {
            binding.mapGoogle.visibility = GONE
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }else{
            try{
                binding.mapGoogle.visibility = VISIBLE
                val resultList = result.contents.split(",")
                val siteLati = resultList[0].toDouble()
                val siteLongi = resultList[1].toDouble()
                siteOid = resultList[2].toLong()
                staticLocation = LatLng(siteLati, siteLongi)
                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }catch (e: Exception){
                Log.d(TAG, "barcodeException: ${e.message}")
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableUserLocation()
        createSiteCircle(Color.BLUE, 0x220000FF, 200.0)
    }


    private fun createSiteCircle(strokeColor: Int, fillColor: Int, radius: Double) {
        val circleOptions = CircleOptions()
            .center(staticLocation)
            .radius(radius) // in meters
            .strokeColor(strokeColor)
            .fillColor(fillColor) // transparent fill
            .strokeWidth(2f)
        mMap.addCircle(circleOptions)
    }

    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                // Show user's location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))

                // Add static marker
                mMap.addMarker(
                    MarkerOptions().position(staticLocation).title("Scan Point Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)) // your icon in drawable
                )

                // Calculate distance
                val distance = calculateDistance(
                    location.latitude,
                    location.longitude,
                    staticLocation.latitude,
                    staticLocation.longitude
                )
//                Toast.makeText(this, "Distance: %.2f km".format(distance), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000.0 // in kilometers
    }
}
// [18.5474829, 73.7510051, 12]