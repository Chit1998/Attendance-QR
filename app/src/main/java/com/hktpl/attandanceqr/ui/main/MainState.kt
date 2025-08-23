package com.hktpl.attandanceqr.ui.main

import com.hktpl.attandanceqr.models.ScanQRResponse
import com.hktpl.attandanceqr.models.ScanResult
import com.hktpl.attandanceqr.models.StopTrackingResponse
import com.hktpl.attandanceqr.models.UpdateLocationResponse

data class ScanQRState(
    val isLoading: Boolean = false,
    val data: ScanQRResponse? = null,
    val error: String? = ""
)

data class AttendanceState(
    val isLoading: Boolean = false,
    val data: ScanResult? = null,
    val error: String? = ""
)

data class UpdateLocationState(
    val isLoading: Boolean = false,
    val data: UpdateLocationResponse? = null,
    val error: String? = ""
)
data class StopState(
    val isLoading: Boolean = false,
    val data: StopTrackingResponse? = null,
    val error: String? = ""
)

