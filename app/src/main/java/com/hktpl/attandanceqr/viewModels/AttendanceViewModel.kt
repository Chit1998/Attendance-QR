package com.hktpl.attandanceqr.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {
    private val markAttendanceData = MutableLiveData<AttendanceMarkModelV1.ScanResult?>()
    private val attendanceListData = MutableLiveData<MutableList<UserModel.AttendanceDataModel>?>()

    val markAttendance : MutableLiveData<AttendanceMarkModelV1.ScanResult?>
        get() = markAttendanceData

    val attendanceList : MutableLiveData<MutableList<UserModel.AttendanceDataModel>?>
        get() = attendanceListData

    fun markAttendanceApi(user: AttendanceMarkModelV1) = viewModelScope.launch {
        try {
            repositoryImpl.markAttendance(user).let { response ->
                if (response.isSuccessful){
                    markAttendanceData.value = response.body()
                }else {
                    markAttendanceData.postValue(null)
                }
            }
        }catch (e: Exception){
            markAttendanceData.postValue(null)
        }
    }

    fun getAttendanceListApi(user: UserModel) = viewModelScope.launch {
        try {
            repositoryImpl.getAttendance(user).let { response ->
                if (response.isSuccessful){
                    attendanceListData.value = response.body()
                }else {
                    attendanceListData.postValue(null)
                }
            }
        }catch (e: Exception){
            attendanceListData.postValue(null)
        }
    }


}