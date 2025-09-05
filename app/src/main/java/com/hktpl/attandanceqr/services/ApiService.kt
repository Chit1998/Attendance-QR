package com.hktpl.attandanceqr.services

import com.hktpl.attandanceqr.models.AttendanceDataModel
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.PvcExpiryDateResetResponseModel
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.models.ScanQR
import com.hktpl.attandanceqr.models.ScanQRResponse
import com.hktpl.attandanceqr.models.ScanResult
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.StopTracking
import com.hktpl.attandanceqr.models.StopTrackingResponse
import com.hktpl.attandanceqr.models.UpdateLocation
import com.hktpl.attandanceqr.models.UpdateLocationResponse
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.models.WeekOffResponseModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("register")
    suspend fun registerEmpInfo(@Body userModel: RegisterUserModel): RegisterUserModel.UserInfoModel?

    @POST("empInfo")
    suspend fun getUserInfo(@Body model: UserModel): UserModel.EmployeeInfoModel?

    @POST("attendance")
    suspend fun getAttendance(@Body model: UserModel): MutableList<AttendanceDataModel>

    @PUT("markAttendance")
    suspend fun markAttendance(@Body model: AttendanceMarkModelV1): ScanResult

    @POST("weeklyoffdayreset")
    suspend fun setWeekOff(@Body model: WeekOffModel): WeekOffResponseModel

    @POST("weeklyoffday")
    suspend fun getWeekOf(@Body model: ShowWeekOffModel): WeekOffResponseModel

    @POST("pvcexpirydate")
    suspend fun getPvcExpiryDate(@Body model: ShowWeekOffModel): PvcExpiryDateResetResponseModel

    @POST("pvcexpirydatereset")
    suspend fun setPvcExpiryDateReset(@Body model: PvcExpiryDateResetModel): PvcExpiryDateResetResponseModel

    @POST("scanqr")
    suspend fun scanqr(@Body model: ScanQR): ScanQRResponse

    @POST("updateLocation")
    suspend fun updateLocation(@Body model: UpdateLocation): UpdateLocationResponse

    @POST("stopTracking")
    suspend fun stopTracking(@Body model: StopTracking): StopTrackingResponse

}