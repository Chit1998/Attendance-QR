package com.hktpl.attandanceqr.objects

import com.hktpl.attandanceqr.fragments.Location1
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object Utils {
    val DISTANCE = 2.0
    fun calculateDistanceBetweenLocations(
        location1: Location1, location2: Location1
    ): Double {
        val lat1 = Math.toRadians(location1.latitude)
        val lon1 = Math.toRadians(location1.longitude)
        val lat2 = Math.toRadians(location2.latitude)
        val lon2 = Math.toRadians(location2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Radius of the Earth in kilometers
        val radius = 6371.0

        return radius * c
    }
}
