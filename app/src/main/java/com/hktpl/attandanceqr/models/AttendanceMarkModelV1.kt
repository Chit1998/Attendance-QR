package com.hktpl.attandanceqr.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceMarkModelV1(
    @SerializedName("empOid")
    val empOid: String?,
    @SerializedName("siteOid")
    val siteOid: Long?,
    @SerializedName("time")
    val time: Long?,
    @SerializedName("geoLat")
    val geoLat: Double?,
    @SerializedName("geoLong")
    val geoLong: Double?
): Serializable {
        data class ScanResult(
            @SerializedName("message")
            val message: String? = ""
        )
}
