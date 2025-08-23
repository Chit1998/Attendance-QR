package com.hktpl.attandanceqr.models

import java.io.Serializable

data class UpdateLocation(
    val empOid: Long,
    val siteOid: Long,
    val geoLat: Double,
    val geoLong: Double,
): Serializable

data class UpdateLocationResponse(
    val success: Boolean,
    val message: String,
    val locationTrackingEnabled: Boolean,
): Serializable

