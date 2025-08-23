package com.hktpl.attandanceqr.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceMarkModelV1(
    @SerializedName("empOid")
    val empOid: String?,
    @SerializedName("siteGateQrcodeOid")
    val siteOid: Long?,
    @SerializedName("geoLat")
    val geoLat: Double?,
    @SerializedName("geoLong")
    val geoLong: Double?
): Serializable

data class ScanResult(
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("siteOid")
    val siteOid: Long? = 0L,
    @SerializedName("locationTrackingEnabled")
    val locationTrackingEnabled: Boolean? = false,

): Serializable
