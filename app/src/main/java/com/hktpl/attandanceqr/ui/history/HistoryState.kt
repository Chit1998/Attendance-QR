package com.hktpl.attandanceqr.ui.history

import com.hktpl.attandanceqr.models.AttendanceDataModel

data class HistoryState(
    val isLoading: Boolean = false,
    val data: MutableList<AttendanceDataModel>? = null,
    val error: String? = ""
)