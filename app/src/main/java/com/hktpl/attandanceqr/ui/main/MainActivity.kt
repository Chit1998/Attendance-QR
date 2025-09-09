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
import com.hktpl.attandanceqr.databinding.AttendanceMarkDialogBinding
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.ScanQRResponse
import com.hktpl.attandanceqr.models.StopTracking
import com.hktpl.attandanceqr.services.LocationService

@AndroidEntryPoint
class MainActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var preferences: UserPreferences
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest
    private var staticLocation: LatLng? = null
    private var siteGateOid = 0L
    private var numberColor: Int = 0
    private var currentCircle: Circle? = null

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
            if (internetStatus == true){
                openQRCode()
            }else{
                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openQRCode() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
            return
        }
        startActivity(Intent(this, ScannerActivity::class.java))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        requestLocationPermission()
    }

    private fun createSiteCircle(
        googleMap: GoogleMap?,
        latLng: LatLng,
        scanQRResponse: ScanQRResponse,
        userLatLng: LatLng
    ) {
        currentCircle?.remove()
        if (numberColor == 0){
            val circleOptions = CircleOptions()
                .center(latLng)
                .radius(scanQRResponse.radius!!.toDouble()) // in meters
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF) // transparent fill
                .strokeWidth(2f)
            currentCircle = googleMap?.addCircle(circleOptions)
        }else {
            val circleOptions = CircleOptions()
                .center(latLng)
                .radius(scanQRResponse.radius!!.toDouble()) // in meters
                .strokeColor(Color.GREEN)
                .fillColor("#2250EF55".toColorInt())
                .strokeWidth(2f)
            currentCircle = googleMap?.addCircle(circleOptions)
            if (numberColor == 1){
                numberColor = 2
                mainViewModel.attendance(
                    AttendanceMarkModelV1(
                        preferences.getOid(),
                        scanQRResponse.siteGateOid,
                        userLatLng.latitude,
                        userLatLng.longitude
                    )
                )
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
        }
    }

    private fun enableUserLocation(latitude: Double, longitude: Double) {
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

        mMap?.isMyLocationEnabled = true
        val userLatLng = LatLng(latitude, longitude)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f))
        if (staticLocation != null){
            // Calculate distance
            val distance = calculateDistance(
                latitude,
                longitude,
                staticLocation!!.latitude,
                staticLocation!!.longitude
            )
            // Add static marker
            mMap?.addMarker(
                MarkerOptions().position(staticLocation!!).title("Scan Point Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)) // your icon in drawable
            )

            mainViewModel.scanQR(ScanQR(siteGateOid))
            mainViewModel.scanQRData.observe(this) { response->
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
                            if(response.data.success){
                                if (distance <= response.data.radius!!.toFloat()) {
                                    if (numberColor != 2){
                                        numberColor = 1
                                        createSiteCircle(
                                            mMap,
                                            staticLocation!!,
                                            response.data,
                                            userLatLng
                                        )
                                    }
                                } else {
                                    numberColor = 0
                                    createSiteCircle(
                                        mMap,
                                        staticLocation!!,
                                        response.data,
                                        userLatLng
                                    )
                                }
                            }else{
                                Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferences = UserPreferences(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        requestLocationPermission()
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

    private fun markAttendance(result: String) {
        val resultList = result.split(",")
        val siteLatitude = resultList[0].toDouble()
        val siteLongitude = resultList[1].toDouble()
        siteGateOid = resultList[2].toLong()
        staticLocation = LatLng(siteLatitude, siteLongitude)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
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
            // Permission already granted
            checkGpsStatus()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
            checkGpsStatus()
        } 
        else {
            // PERMISSION NOT GRANTED
        }
    }

    override fun onPause() {
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
        }else{
            stopLocationService()
        }
        super.onPause()
    }
    private fun checkGpsStatus() {
        val locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, prompt the user to enable it
//            val enableGpsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivityForResult(enableGpsIntent, GPS_REQUEST_CODE)
            Toast.makeText(this, "Enable GPS", Toast.LENGTH_SHORT).show()
        } else {
            // GPS is enabled, fetch location
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationRequest = LocationRequest.Builder(100L)
                .setWaitForAccurateLocation(true)
                .setIntervalMillis(500)
                .setMinUpdateIntervalMillis(500)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallBack, Looper.getMainLooper())
        }
    }

    private val locationCallBack = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
//            lat = p0.lastLocation!!.latitude
//            lon = p0.lastLocation!!.longitude
            enableUserLocation(p0.lastLocation!!.latitude,p0.lastLocation!!.longitude)
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
