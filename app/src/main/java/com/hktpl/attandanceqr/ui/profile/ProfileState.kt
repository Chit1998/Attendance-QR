package com.hktpl.attandanceqr.ui.profile

import com.hktpl.attandanceqr.models.PvcExpiryDateResetResponseModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffResponseModel

data class ProfileState(
    val isLoading: Boolean = false,
    val data: UserModel.EmployeeInfoModel? = null,
    val error: String? = ""
)

data class WeekOfState(
    val isLoading: Boolean = false,
    val data: WeekOffResponseModel? = null,
    val error: String? = ""
)

data class PVCState(
    val isLoading: Boolean = false,
    val data: PvcExpiryDateResetResponseModel? = null,
    val error: String? = ""
)
