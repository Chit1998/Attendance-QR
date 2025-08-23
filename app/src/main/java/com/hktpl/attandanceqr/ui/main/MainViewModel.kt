package com.hktpl.attandanceqr.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.ScanQR
import com.hktpl.attandanceqr.models.StopTracking
import com.hktpl.attandanceqr.models.StopTrackingResponse
import com.hktpl.attandanceqr.models.UpdateLocation
import com.hktpl.attandanceqr.models.UpdateLocationResponse
import com.hktpl.attandanceqr.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){

    private val _scanQRData: MutableLiveData<ScanQRState?> =
        MutableLiveData(ScanQRState())

    val scanQRData: MutableLiveData<ScanQRState?>
        get() = _scanQRData

    fun scanQR(scanQR: ScanQR) = viewModelScope.launch {
        _scanQRData.value = ScanQRState(isLoading = false)
        repositoryImpl.scanqr(scanQR).let { response ->
            when(response){
                is Resource.Loading->{
                    _scanQRData.value = ScanQRState(isLoading = true)
                }
                is Resource.Success->{
                    response.data!!.let {
                        _scanQRData.value = ScanQRState(data = it)
                    }
                }
                is Resource.Error->{
                    _scanQRData.value = ScanQRState(error = "Somthing won't wrong.")
                }
            }
        }
    }

    private val _attendanceData: MutableLiveData<AttendanceState?> =
        MutableLiveData(AttendanceState())

    val attendanceData: MutableLiveData<AttendanceState?>
        get() = _attendanceData

    fun attendance(attendance: AttendanceMarkModelV1) = viewModelScope.launch {
        _attendanceData.value = AttendanceState(isLoading = false)
        repositoryImpl.markAttendance(attendance).let { response ->
            when(response){
                is Resource.Loading->{
                    _attendanceData.value = AttendanceState(isLoading = true)
                }
                is Resource.Success->{
                    response.data!!.let {
                        _attendanceData.value = AttendanceState(data = it)
                    }
                }
                is Resource.Error->{
                    _attendanceData.value = AttendanceState(error = "Somthing won't wrong.")
                }
            }
        }
    }

    private val _updateLocationData: MutableLiveData<UpdateLocationState?> = MutableLiveData(UpdateLocationState())

    val updateLocationData: MutableLiveData<UpdateLocationState?>
        get() = _updateLocationData

    fun userUpdateLocation(updateLocation: UpdateLocation) = viewModelScope.launch {
        _updateLocationData.value = UpdateLocationState(isLoading = false)
        repositoryImpl.updateLocation(updateLocation).let { response ->
            when(response){
                is Resource.Loading->{
                    _updateLocationData.value = UpdateLocationState(isLoading = true)
                }
                is Resource.Success->{
                    response.data!!.let {
                        _updateLocationData.value = UpdateLocationState(data = it)
                    }
                }
                is Resource.Error->{
                    _updateLocationData.value = UpdateLocationState(error = "Somthing won't wrong.")
                }
            }
        }
    }

    private val _stopTrackingData: MutableLiveData<StopState?> = MutableLiveData(StopState())

    val stopTrackingData: MutableLiveData<StopState?>
        get() = _stopTrackingData

    fun stopTracking(stopTracking: StopTracking) = viewModelScope.launch {
        _stopTrackingData.value = StopState(isLoading = false)
        repositoryImpl.stopTracking(stopTracking).let { response ->
            when(response){
                is Resource.Loading->{
                    _stopTrackingData.value = StopState(isLoading = true)
                }
                is Resource.Success->{
                    response.data!!.let {
                        _stopTrackingData.value = StopState(data = it)
                    }
                }
                is Resource.Error->{
                    _stopTrackingData.value = StopState(error = "Somthing won't wrong.")
                }
            }
        }
    }


}