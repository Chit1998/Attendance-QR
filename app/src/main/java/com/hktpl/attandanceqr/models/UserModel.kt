package com.hktpl.attandanceqr.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(
    @SerializedName("empId")
    val empId: String?
): Serializable {
    data class EmployeeInfoModel(
        @SerializedName("name")
        val name: String?,
        @SerializedName("empId")
        val empId: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("phoneNo")
        val phoneNo: String?,
        @SerializedName("message")
        val message: String?,
        @SerializedName("date")
        val date: Long?,
        @SerializedName("inTime")
        val inTime: Long?,
        @SerializedName("outTime")
        val outTime: Long?,
        @SerializedName("markedBy")
        val markedBy: String?,
        @SerializedName("reason")
        val reason: String?,
    ): Serializable

    data class AttendanceDataModel(
        @SerializedName("inTime")
        val inTime: Long?,
        @SerializedName("outTime")
        val outTime: Long,
        @SerializedName("dated")
        val dated: Long?,
        @SerializedName("markedBy")
        val markedBy: String?,
        @SerializedName("reason")
        val reason: String?,
        @SerializedName("inSite")
        val inSite: String?,
        @SerializedName("outSite")
        val outSite: String?
    )

    data class MsgModel(
        @SerializedName("message")
        val message: String?
    ):Serializable
}