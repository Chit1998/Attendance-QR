package com.hktpl.attandanceqr.di.repo

import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.PvcExpiryDateResetResponseModel
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.models.WeekOffResponseModel
import com.hktpl.attandanceqr.objects.Resource
import com.hktpl.attandanceqr.services.ApiService
import javax.inject.Inject

class RestRepositoryImpl @Inject constructor(private val api: ApiService) {

    suspend fun registerUser(user: RegisterUserModel): Resource<RegisterUserModel.UserInfoModel?>{
        return try {
            val result = api.registerEmpInfo(userModel = user)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }
    suspend fun getUserInfo(model: UserModel): Resource<UserModel.EmployeeInfoModel?>{
        return try {
            val result = api.getUserInfo(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

    suspend fun getAttendance(model: UserModel): Resource<MutableList<UserModel.AttendanceDataModel>>{
        return try {
            val result = api.getAttendance(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

//    suspend fun getRole(model: UserModel): Resource<UserModel.MsgModel>{
//        return try {
//            val result = api.getRole(model = model)
//            Resource.Success(data = result)
//        }catch (e: Exception){
//            Resource.Error(msg = e.message.toString())
//        }
//    }

    suspend fun markAttendance(model: AttendanceMarkModelV1): Resource<AttendanceMarkModelV1.ScanResult>{
        return try {
            val result = api.markAttendance(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

    suspend fun setWeekOff(model: WeekOffModel): Resource<WeekOffResponseModel>{
        return try {
            val result = api.setWeekOff(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

    suspend fun getWeekOf(model: ShowWeekOffModel) : Resource<WeekOffResponseModel>{
        return try {
            val result = api.getWeekOf(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

    suspend fun getPvcExpiryDate(model: ShowWeekOffModel) : Resource<PvcExpiryDateResetResponseModel>{
        return try {
            val result = api.getPvcExpiryDate(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }

    suspend fun setPvcExpiryDateReset(model: PvcExpiryDateResetModel): Resource<PvcExpiryDateResetResponseModel>{
        return try {
            val result = api.setPvcExpiryDateReset(model = model)
            Resource.Success(data = result)
        }catch (e: Exception){
            Resource.Error(msg = e.message.toString())
        }
    }
    
}