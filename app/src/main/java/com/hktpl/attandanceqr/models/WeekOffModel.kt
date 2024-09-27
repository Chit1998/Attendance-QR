package com.hktpl.attandanceqr.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeekOffModel(
    @SerializedName("empOid")
    val empOid: Long,
    @SerializedName("weeklyOffDay")
    val weeklyOffDay: String,
) : Serializable

data class ShowWeekOffModel(
    @SerializedName("empOid")
    val empOid: Long
) : Serializable

data class PvcExpiryDateResetModel(
    @SerializedName("empOid")
    val empOid: Long,
    @SerializedName("pvcExpiryDate")
    val pvcExpiryDate: Long
) : Serializable

data class WeekOffResponseModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("weeklyOffDay")
    val weeklyOffDay: String?
)

data class PvcExpiryDateResetResponseModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("pvcExpiryDate")
    val pvcExpiryDate: Long?
)



