package com.hktpl.attandanceqr.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityMainBinding
import com.hktpl.attandanceqr.models.ScanQR
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.ui.ScannerActivity
import com.hktpl.attandanceqr.ui.history.HistoryActivity
import com.hktpl.attandanceqr.ui.profile.ProfileActivity
import com.hktpl.attandanceqr.utils.CalculateDistance.calculateDistance
import com.hktpl.attandanceqr.utils.scanner.QRListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import androidx.core.graphics.toColorInt
import com.google.android.gms.maps.model.Marker
import com.hktpl.attandanceqr.databinding.AttendanceMarkDialogBinding
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.ScanQRResponse
import com.hktpl.attandanceqr.models.StopTracking
import com.hktpl.attandanceqr.services.LocationService

@AndroidEntryPoint
class MainActivity : BaseActivity(), OnMapReadyCallback {
//    private lateinit var binding: ActivityMainBinding
//    private val mainViewModel: MainViewModel by viewModels()
//    private lateinit var preferences: UserPreferences
//    private var mMap: GoogleMap? = null
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private lateinit var locationManager: LocationManager
//    private lateinit var locationRequest: LocationRequest
////    private var staticLocation: LatLng? = null
//    private var siteGateOid = 0L
//    private var numberColor: Int = 0
//    private var currentCircle: Circle? = null

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var preferences: UserPreferences

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var siteGateOid = 0L
    private var isScanApiCalled = false
    private var siteData: ScanQRResponse? = null
    private var currentCircle: Circle? = null
    private var siteMarker: Marker? = null
    private var isAttendanceMarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolBar.txtToolbarTitle.text = getString(R.string.home)
        preferences = UserPreferences(this)
        binding.userData = preferences
        binding.txtAppVersion.text = appVersion
        binding.txtClock.text = Calendar.DATE.toString()
//        // Blur effect for Android 12+ (improved in Android 16) fragments
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            container.setRenderEffect(
//                RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
//            )
//        }
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
        binding.fabButton.setOnClickListener {
            openQRCode()
        }

        observeScanQR()
        observeAttendance()
    }

    // ============================================================
    // MAP READY
    // ============================================================
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        requestLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        preferences = UserPreferences(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        requestLocationPermission()
        binding.btnAttendance.visibility = GONE
        obj = object : QRListener{
            override fun qrResults(result: String?) {
                if (result!!.isEmpty()) {
                    binding.mapGoogle.visibility = GONE
                    binding.btnAttendance.visibility = GONE
                    Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
                }else{
                    try{
                        binding.mapGoogle.visibility = VISIBLE
                        markAttendance(result)
                        binding.fabButton.visibility = GONE
                        binding.btnAttendance.visibility = VISIBLE
                    }catch (e: Exception){
                        Toast.makeText(this@MainActivity, "You are not scanning write scanner.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "barcodeException: ${e.message}")
                    }
                }
            }
        }
    }

    // ============================================================
    // QR SCAN
    // ============================================================
    private fun openQRCode() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
            return
        }

        startActivity(Intent(this, ScannerActivity::class.java))
    }

    private fun markAttendance(result: String) {

        isScanApiCalled = false
        val resultList = result.split(",")
        siteGateOid = resultList[2].toLong()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapGoogle) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            startLocationUpdates()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) startLocationUpdates()
        }

    private fun startLocationUpdates() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return
            val userLatLng = LatLng(location.latitude, location.longitude)
            updateUserOnMap(userLatLng)
            // Call scanQR API only once
            if (!isScanApiCalled && siteGateOid != 0L) {
                isScanApiCalled = true
                mainViewModel.scanQR(ScanQR(siteGateOid))
            }

            // After API success, update circle color only
            siteData?.let {
                drawOrUpdateCircle(it, userLatLng)
            }
        }
    }

    private fun updateUserOnMap(userLatLng: LatLng) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        mMap?.isMyLocationEnabled = true
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))
    }

    // ============================================================
    // API OBSERVER
    // ============================================================

    private fun observeScanQR() {
        mainViewModel.scanQRData.observe(this) { response ->
            if (response == null) return@observe
            if (response.data == null) return@observe
            if (response.data.success != true) return@observe

            response.data.let {
                val longitude = it.longitude
                val latitude = it.latitude
                if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
                    Toast.makeText(this, "Invalid site location", Toast.LENGTH_SHORT).show()
                    return@observe
                }
                siteData = response.data
                val siteLatLng = LatLng(
                    latitude.toDouble(),
                    longitude.toDouble()
                )
                // Add marker only once
                if (siteMarker == null && mMap != null) {
                    siteMarker = mMap!!.addMarker(
                        MarkerOptions()
                            .position(siteLatLng)
                            .title("Scan Point")
                            .icon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                            ))
                    )
                }
            }
        }
    }

    // ============================================================
    // CIRCLE LOGIC
    // ============================================================

    private fun drawOrUpdateCircle(
        scanQRResponse: ScanQRResponse,
        userLatLng: LatLng
    ) {

        val siteLatLng = LatLng(
            scanQRResponse.latitude!!.toDouble(),
            scanQRResponse.longitude!!.toDouble()
        )

        val distance = calculateDistance(
            userLatLng.latitude,
            userLatLng.longitude,
            siteLatLng.latitude,
            siteLatLng.longitude
        )

        val isInside = distance <= scanQRResponse.radius!!.toFloat()

        val strokeColor = if (isInside) Color.GREEN else Color.BLUE
        val fillColor = if (isInside)
            "#2250EF55".toColorInt()
        else
            0x220000FF

        if (isInside && !isAttendanceMarked) {
            isAttendanceMarked = true   // ✅ Prevent multiple calls
            mainViewModel.attendance(AttendanceMarkModelV1(
                preferences.getOid(),
                siteGateOid,
                userLatLng.latitude,
                userLatLng.longitude
            ))
        }
        currentCircle?.remove()

        currentCircle = mMap?.addCircle(
            CircleOptions()
                .center(siteLatLng)
                .radius(scanQRResponse.radius!!.toDouble())
                .strokeColor(strokeColor)
                .fillColor(fillColor)
                .strokeWidth(3f)
        )
    }

    private fun observeAttendance() {
        mainViewModel.attendanceData.observe(this) { response ->
            if (response == null) return@observe
            if (response.isLoading) {
                binding.progressBarMain.visibility = VISIBLE
                return@observe
            }
            if (!response.error.isNullOrEmpty()) {
                binding.progressBarMain.visibility = GONE
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                isAttendanceMarked = false   // allow retry if failed
                return@observe
            }
            response.data?.let { data ->
                binding.progressBarMain.visibility = GONE
                openAttendanceDialog(data.message ?: "")
                if (data.message == getString(R.string.in_time_marked)) {
                    if (data.locationTrackingEnabled == true) {
                        preferences.setSiteOid(data.siteOid.toString())
                        preferences.setLocationStatus(true)
                    } else {
                        preferences.setLocationStatus(false)
                    }
                } else {
                    stopLocationService()
                    preferences.setLocationStatus(false)
                    mainViewModel.stopTracking(
                        StopTracking(preferences.getOid()!!.toLong())
                    )
                }
            }
        }

        mainViewModel.attendanceData.observe(this){ response ->
            if (response != null){
                if (response.isLoading){
                    binding.progressBarMain.visibility = VISIBLE
                    binding.progressBarMain.progress
                }
                if (response.error!!.isNotEmpty()){
                    binding.progressBarMain.visibility = GONE
                    if (internetStatus) {
                        Toast.makeText(this, "${mainViewModel.scanQRData.value?.error}", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                    }
                }

                if (response.data != null){
                    binding.progressBarMain.visibility = GONE
                    if (internetStatus) {
                        openAttendanceDialog(response.data.message.toString())
                        if (response.data.message == getString(R.string.in_time_marked)){
                            if (response.data.locationTrackingEnabled!!){
                                preferences.setSiteOid(response.data.siteOid.toString())
                                preferences.setLocationStatus(true)
                            }else{
                                preferences.setLocationStatus(false)
                            }
                        }else{
                            stopLocationService()
                            preferences.setLocationStatus(false)
                            mainViewModel.stopTracking(StopTracking(preferences.getOid()!!.toLong()))
                        }
                    }else{
                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    companion object{
        var obj: QRListener? = null
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent) // Required for Android 8+
        } else {
            startService(intent)
        }
    }

    private fun stopLocationService() {
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
    }

    private fun openAttendanceDialog(message: String){
        val dialogBinding = AttendanceMarkDialogBinding.inflate(layoutInflater, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        dialog.setCancelable(false)

        dialogBinding.tvTitle.text = message
        if (message == getString(R.string.in_time_marked)){
            dialogBinding.ivSuccess.visibility = VISIBLE
            dialogBinding.ivCancel.visibility = GONE
        }else if (message == getString(R.string.out_time_marked)){
            dialogBinding.ivSuccess.visibility = VISIBLE
            dialogBinding.ivCancel.visibility = GONE
        }else {
            dialogBinding.ivCancel.visibility = VISIBLE
            dialogBinding.ivSuccess.visibility = GONE
        }

        dialogBinding.btnOk.setOnClickListener {
            if (preferences.getLocationStatus() ==  true){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.FOREGROUND_SERVICE_LOCATION
                            ),
                            100
                        )
                    }else{
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            100
                        )
                    }
                } else {
                    startLocationService()
                }
            }
            dialog.dismiss()
            dialog.cancel()
        }
        dialog.show()
    }
}
