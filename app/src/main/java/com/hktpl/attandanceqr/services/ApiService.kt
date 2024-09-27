package com.hktpl.attandanceqr.services

import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.PvcExpiryDateResetResponseModel
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.models.WeekOffResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("register")
    suspend fun registerEmpInfo(@Body userModel: RegisterUserModel): Response<RegisterUserModel.UserInfoModel?>

    @POST("empInfo")
    suspend fun getUserInfo(@Body model: UserModel): Response<UserModel.EmployeeInfoModel?>

    @POST("attendance")
    suspend fun getAttendance(@Body model: UserModel): Response<MutableList<UserModel.AttendanceDataModel>?>

    @PUT("attendanceV1")
    suspend fun markAttendance(@Body model: AttendanceMarkModelV1): Response<AttendanceMarkModelV1.ScanResult>

    @POST("role")
    suspend fun getRole(@Body model: UserModel): Response<UserModel.MsgModel>

    @POST("weeklyoffdayreset")
    suspend fun setWeekOff(@Body model: WeekOffModel): Response<WeekOffResponseModel>

    @POST("weeklyoffday")
    suspend fun getWeekOf(@Body model: ShowWeekOffModel): Response<WeekOffResponseModel>

    @POST("pvcexpirydate")
    suspend fun getPvcExpiryDate(@Body model: ShowWeekOffModel): Response<PvcExpiryDateResetResponseModel>

    @POST("pvcexpirydatereset")
    suspend fun setPvcExpiryDateReset(@Body model: PvcExpiryDateResetModel): Response<PvcExpiryDateResetResponseModel>


}