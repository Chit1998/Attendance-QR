package com.hktpl.attandanceqr.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.UpdateLocation
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class LocationService : Service(){

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var preferences: UserPreferences
    private var lastApiCallTime: Long = 0L // Track last call

    @Inject
    lateinit var mainUseCase: RestRepositoryImpl
    override fun onCreate() {
        super.onCreate()
        preferences = UserPreferences(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            5000L
        ).setMinUpdateIntervalMillis(2000L).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastApiCallTime >= 3 * 60 * 1000) { // 3 minutes
                        lastApiCallTime = currentTime
                        val response = CoroutineScope(Dispatchers.IO).launch {
                            try {
                                mainUseCase.updateLocation(UpdateLocation(
                                    preferences.getOid()!!.toLong(),
                                    preferences.getSiteOid()!!.toLong(),
                                    location.latitude,
                                    location.longitude
                                ))
                            }catch (e: Exception) {
                                Log.e("BgLocation", "Error sending location", e)
                            }
                        }
                        Log.d("TAG_DATA", "onLocationResult: $response")
                    }else {
                        Log.d("BgLocation", "Location received but API not called yet (waiting 5 mins)")
                    }
                    Log.d("BgLocation", "Lat: ${location.latitude}, Lng: ${location.longitude}")
                }
            }
        }

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun createNotification(): Notification {
        val channelId = "location_channel"
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId, "Location Updates", NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Location Service")
            .setContentText("Tracking location in background")
            .setSmallIcon(R.drawable.humankind_logo)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        if (::locationCallback.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}