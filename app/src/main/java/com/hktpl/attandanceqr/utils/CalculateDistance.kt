package com.hktpl.attandanceqr.utils

import android.location.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object CalculateDistance {
    fun distanceCalculator(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double): Double{
        val R = 6371000.0 // Earth radius in meters

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val rLat1 = Math.toRadians(lat1)
        val rLat2 = Math.toRadians(lat2)

        val a = sin(dLat / 2).pow(2.0) +
                cos(rLat1) * cos(rLat2) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c // distance in meters
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Float{
        val startLocation = Location("").apply {
            latitude = lat1
            longitude = lon1
        }

        val endLocation = Location("").apply {
            latitude = lat2
            longitude = lon2
        }

        return startLocation.distanceTo(endLocation) // Returns distance in meters
    }
}