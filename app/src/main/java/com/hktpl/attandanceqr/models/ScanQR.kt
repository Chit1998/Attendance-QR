package com.hktpl.attandanceqr.models

import java.io.Serializable

data class ScanQR(
    val siteGateOid: Long,
): Serializable

data class ScanQRResponse(
    val success: Boolean,
    val message: String,
    val siteGateOid: Long?,
    val siteGateName: String?,
    val latitude: String?,
    val longitude: String?,
    val radius: String?,
): Serializable