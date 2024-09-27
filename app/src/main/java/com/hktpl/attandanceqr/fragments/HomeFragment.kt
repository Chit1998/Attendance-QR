package com.hktpl.attandanceqr.fragments

import android.Manifest
import android.app.Dialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.zxing.integration.android.IntentIntegrator
import com.hktpl.attandanceqr.BaseFragment
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.activities.MainActivity
import com.hktpl.attandanceqr.activities.ProfileActivity
import com.hktpl.attandanceqr.databinding.FragmentHomeBinding
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.objects.AlertObject
import com.hktpl.attandanceqr.objects.TAG.GPS_REQUEST_CODE
import com.hktpl.attandanceqr.objects.TAG.LOCATION_PERMISSION_CODE
import com.hktpl.attandanceqr.objects.Utils.DISTANCE
import com.hktpl.attandanceqr.objects.Utils.calculateDistanceBetweenLocations
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.services.LocationService
import com.hktpl.attandanceqr.viewModels.AttendanceViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var mainActivity: MainActivity
    private lateinit var locationManager: LocationManager
    private var locationService: LocationService = LocationService()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var preferences: UserPreferences
    private val viewmodel: AttendanceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        mainActivity = activity as MainActivity
        preferences = UserPreferences(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationManager =
            requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        locationService.getUserLocation(requireContext(), locationManager, this)
        binding.txtAppVersion.text = "Version Code: ${requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName}"
        return binding.root
    }


    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
//this code is for
        val integrator = IntentIntegrator(this.mainActivity)
        integrator.setOrientationLocked(true)
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val resultList = result.contents.split(",")
            val jobLati = resultList[0].toDouble()
            val jobLongi = resultList[1].toDouble()
            if (calculateDistanceBetweenLocations(
                    Location1(
                        (activity as MainActivity).myLatitude,
                        (activity as MainActivity).myLongitude
                    ),
                    Location1(jobLati, jobLongi)
                ) <= DISTANCE
            ) {
                val attendanceMarkModel = AttendanceMarkModelV1(
                    preferences.getOid(),
                    resultList[2].toLong(),
                    Date().time,
                    (activity as MainActivity).myLatitude,
                    (activity as MainActivity).myLongitude
                )
                viewmodel.markAttendanceApi(attendanceMarkModel)
                viewmodel.markAttendance.observe(viewLifecycleOwner){ res ->
                    if (res != null){
                        val dialog = Dialog(requireActivity())
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.singlerow_in_out)
                        val yesBtn: Button = dialog.findViewById(R.id.btn_ok)
                        val tvMsg: TextView = dialog.findViewById(R.id.tv_title)
                        tvMsg.text = res.message.toString()
                        yesBtn.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
                    }else {
                        AlertObject.showAlert(context, "Mark attendance", "FAILED !")
                    }
                }
            } else {
                //message(context, getString(R.string.not_at_location))
                AlertObject.showAlert(
                    context,
                    "Location Scanner",
                    getString(R.string.not_at_location)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermission()

        binding.fabButton.setOnClickListener {
            if ((activity as MainActivity).myLatitude != 0.0 && (activity as MainActivity).myLongitude != 0.0) {
                barcodeLauncher.launch(ScanOptions())
            } else {
                //message(context, "Restart your application")
                AlertObject.showAlert(
                    context,
                    "Location Service",
                    "Close the app completely and start again"
                )
            }
        }
        binding.imgMenu.setOnClickListener {
            val popupMenu = PopupMenu(it.context,binding.imgMenu)
            popupMenu.menu.add("My Profile")
                .setOnMenuItemClickListener {
                    startActivity(Intent(requireContext(), ProfileActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
            popupMenu.show()
        }

        if (preferences.getUserId() != null) {
            binding.txtCodeValue.text = preferences.getUserId()
        }
        if (preferences.getName() != null) {
            binding.txtNameValue.text = preferences.getName()
        }
        binding.txtClock.text = Calendar.DATE.toString()
    }

    override fun onResume() {
        super.onResume()
        mainActivity = activity as MainActivity
    }
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
        } else {
            // PERMISSION NOT GRANTED
        }
    }

    private fun checkGpsStatus() {
        val locationManager =
            requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, prompt the user to enable it
            val enableGpsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(enableGpsIntent, GPS_REQUEST_CODE)
        } else {
            // GPS is enabled, fetch location
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // You have the location
                        mainActivity.myLatitude = location.latitude
                        mainActivity.myLongitude = location.longitude
                    }
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GPS_REQUEST_CODE) {
            // Check if GPS is enabled after the user's action
            checkGpsStatus()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                checkGpsStatus()
            } else {
                // Permission denied
                // Handle the case where the user denied the location permission
            }
        }
    }
}


data class Location1(val latitude: Double, val longitude: Double)