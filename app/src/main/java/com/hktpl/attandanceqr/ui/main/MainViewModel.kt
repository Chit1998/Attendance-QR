package com.hktpl.attandanceqr.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.AttendanceMarkModelV1
import com.hktpl.attandanceqr.models.ScanQR
import com.hktpl.attandanceqr.models.StopTracking
import com.hktpl.attandanceqr.models.UpdateLocation
import com.hktpl.attandanceqr.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){

    private val _scanQRData: MutableLiveData<ScanQRState?> =
        MutableLiveData(ScanQRState())

    val scanQRData: LiveData<ScanQRState?>
        get() = _scanQRData

    fun scanQR(scanQR: ScanQR) = viewModelScope.launch {
        _scanQRData.value = ScanQRState(isLoading = false)

        // Run repository work on IO thread
        val response = withContext(Dispatchers.IO) {
            repositoryImpl.scanqr(scanQR)
        }
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

    private val _attendanceData: MutableLiveData<AttendanceState?> =
        MutableLiveData(AttendanceState())

    val attendanceData: LiveData<AttendanceState?>
        get() = _attendanceData

    fun attendance(attendance: AttendanceMarkModelV1) = viewModelScope.launch {
        _attendanceData.value = AttendanceState(isLoading = false)
        val response = withContext(Dispatchers.IO){
            repositoryImpl.markAttendance(attendance)
        }
        when(response){
            is Resource.Loading->{
                _attendanceData.value = AttendanceState(isLoading = true)
            }
            is Resource.Success->{
                withContext(Dispatchers.Main){
                    response.data!!.let {
                        _attendanceData.value = AttendanceState(data = it)
                    }
                }
            }
            is Resource.Error->{
                _attendanceData.value = AttendanceState(error = "Somthing won't wrong.")
            }
        }
    }

    private val _updateLocationData: MutableLiveData<UpdateLocationState?> = MutableLiveData(UpdateLocationState())

    val updateLocationData: LiveData<UpdateLocationState?>
        get() = _updateLocationData

    fun userUpdateLocation(updateLocation: UpdateLocation) = viewModelScope.launch {
        _updateLocationData.value = UpdateLocationState(isLoading = false)
        val response = withContext(Dispatchers.IO){
            repositoryImpl.updateLocation(updateLocation)
        }
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

    private val _stopTrackingData: MutableLiveData<StopState?> = MutableLiveData(StopState())

    val stopTrackingData: LiveData<StopState?>
        get() = _stopTrackingData

    fun stopTracking(stopTracking: StopTracking) = viewModelScope.launch {
        _stopTrackingData.value = StopState(isLoading = false)
        val response = withContext(Dispatchers.IO){
            repositoryImpl.stopTracking(stopTracking)
        }
        when(response){
            is Resource.Loading->{
                _stopTrackingData.value = StopState(isLoading = true)
            }
            is Resource.Success->{
                withContext(Dispatchers.Main){
                    response.data!!.let {
                        _stopTrackingData.value = StopState(data = it)
                    }
                }
            }
            is Resource.Error->{
                _stopTrackingData.value = StopState(error = "Somthing won't wrong.")
            }
        }
    }
}