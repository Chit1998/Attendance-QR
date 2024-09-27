package com.hktpl.attandanceqr.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegisterUserModel(
    @SerializedName("mobile")
    val empId: String
): Serializable {
    data class UserInfoModel(
        @SerializedName("name")
        val name: String?,
        @SerializedName("empId")
        val empId: String?,
        @SerializedName("email")
        val email: String,
        @SerializedName("oid")
        val oid: String,
        @SerializedName("phoneNo")
        val phoneNo: String,
        @SerializedName("message")
        val message: String?
    ): Serializable
}