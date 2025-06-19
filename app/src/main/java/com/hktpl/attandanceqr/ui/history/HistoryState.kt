package com.hktpl.attandanceqr.ui.history

import com.hktpl.attandanceqr.models.UserModel

data class HistoryState(
    val isLoading: Boolean = false,
    val data: MutableList<UserModel.AttendanceDataModel>? = null,
    val error: String? = ""
)