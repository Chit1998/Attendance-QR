package com.hktpl.attandanceqr.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.hktpl.attandanceqr.fragments.HomeFragment

class LocationService {
    fun getUserLocation(
        context: Context,
        locationManager: LocationManager,
        homeFragment: HomeFragment
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10,
            1f,
            object : LocationListener{
                override fun onLocationChanged(location: Location) {
                    homeFragment.mainActivity.myLatitude = location.latitude
                    homeFragment.mainActivity.myLongitude = location.longitude
                }

                override fun onProviderEnabled(provider: String) {
                    Log.d("TAG_LOCATION", "onProviderEnabled: $provider")
                }

                override fun onProviderDisabled(provider: String) {
                    super.onProviderDisabled(provider)
                    Log.d("TAG_LOCATION", "onProviderDisabled: $provider")
                }
            }
        )
//        { location: Location ->
//            val builder: StringBuilder = java.lang.StringBuilder()
//            builder.append(location.latitude)
//                .append(" ")
//                .append(location.longitude)
//              homeFragment.mainActivity.myLatitude = location.latitude
//              homeFragment.mainActivity.myLongitude = location.longitude
            //Toast.makeText(context, builder.toString(), Toast.LENGTH_SHORT).show()

//        }
//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            10,
//            0.5f,
//            locationListener
//        )
//        homeFragment.mainActivity.myLatitude = latitude
//        homeFragment.mainActivity.myLongitude = longitude
    }

}