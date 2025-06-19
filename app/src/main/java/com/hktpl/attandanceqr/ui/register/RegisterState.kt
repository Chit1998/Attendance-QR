package com.hktpl.attandanceqr.ui.register

import com.hktpl.attandanceqr.models.RegisterUserModel

data class RegisterState(
    val isLoading: Boolean = false,
    val data: RegisterUserModel.UserInfoModel? = null,
    val error: String? = ""
)
