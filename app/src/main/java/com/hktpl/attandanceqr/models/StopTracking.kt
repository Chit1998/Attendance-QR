package com.hktpl.attandanceqr.models

import java.io.Serializable

data class StopTracking(
    val empOid: Long,
): Serializable

data class StopTrackingResponse(
    val success: Boolean,
    val message: String,
): Serializable

