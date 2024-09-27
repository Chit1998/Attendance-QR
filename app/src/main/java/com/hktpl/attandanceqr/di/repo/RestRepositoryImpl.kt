package com.hktpl.attandanceqr.di.repo

import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.services.ApiService
import javax.inject.Inject

class RestRepositoryImpl @Inject constructor(private val api: ApiService) {

    suspend fun registerUser(user: RegisterUserModel) = api.registerEmpInfo(
        RegisterUserModel(
            user.empId
        ))

    suspend fun getUserInfo(model: UserModel) = api.getUserInfo(
        UserModel(
            model.empId
        ))

    suspend fun getAttendance(model: UserModel) = api.getAttendance(
        UserModel(
            model.empId
        ))

    suspend fun getRole(model: UserModel) = api.getRole(
        UserModel(
            model.empId
        ))

    suspend fun markAttendance(model: AttendanceMarkModelV1) = api.markAttendance(
        AttendanceMarkModelV1(
            model.empOid,
            model.siteOid,
            model.time,
            model.geoLat,
            model.geoLong
        ))

    suspend fun setWeekOff(model: WeekOffModel) = api.setWeekOff(
        WeekOffModel(
            model.empOid,
            model.weeklyOffDay
        ))

    suspend fun getWeekOf(model: ShowWeekOffModel) = api.getWeekOf(
        ShowWeekOffModel(
            model.empOid
        ))
    suspend fun getPvcExpiryDate(model: ShowWeekOffModel) = api.getPvcExpiryDate(
        ShowWeekOffModel(
            model.empOid
        ))
    suspend fun setPvcExpiryDateReset(model: PvcExpiryDateResetModel) = api.setPvcExpiryDateReset(
        PvcExpiryDateResetModel(
            model.empOid,
            model.pvcExpiryDate
        ))
    
}