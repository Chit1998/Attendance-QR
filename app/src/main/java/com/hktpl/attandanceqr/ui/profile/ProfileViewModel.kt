package com.hktpl.attandanceqr.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){

    private val _userData: MutableLiveData<ProfileState?> = MutableLiveData(ProfileState())

    val userData: LiveData<ProfileState?>
        get() = _userData

    private val _weekOfData: MutableLiveData<WeekOfState?> = MutableLiveData(WeekOfState())

    val weekOfData: LiveData<WeekOfState?>
        get() = _weekOfData

    private val _pvcData: MutableLiveData<PVCState?> = MutableLiveData(PVCState())

    val pvcData: LiveData<PVCState?>
        get() = _pvcData

    fun getProfileDetails(user: UserModel) = viewModelScope.launch {
        _userData.value = ProfileState(isLoading = true)
        val response = withContext(Dispatchers.IO){ repositoryImpl.getUserInfo(user) }
        when(response){
            is Resource.Loading->{
                _userData.value = ProfileState(isLoading = true)
            }
            is Resource.Success->{
                response.data!!.let {
                    _userData.value = ProfileState(data = it)
                }
            }
            is Resource.Error->{
                _userData.value = ProfileState(error = "Somthing won't wrong.")
            }
        }
    }

    fun getWeekOf(weekOf: ShowWeekOffModel) = viewModelScope.launch {
        _weekOfData.value = WeekOfState(isLoading = true)
        val response = withContext(Dispatchers.IO){
            repositoryImpl.getWeekOf(weekOf)
        }
        when(response){
            is Resource.Loading->{
                _weekOfData.value = WeekOfState(isLoading = true)
            }
            is Resource.Success->{
                response.data!!.let {
                    _weekOfData.value = WeekOfState(data = it)
                }
            }
            is Resource.Error->{
                _weekOfData.value = WeekOfState(error = "Somthing won't wrong.")
            }
        }
    }

    fun getPvc(pvc: ShowWeekOffModel) = viewModelScope.launch {
        _pvcData.value = PVCState(isLoading = true)
        val response = withContext(Dispatchers.IO){ repositoryImpl.getPvcExpiryDate(pvc) }
        when(response){
            is Resource.Loading->{
                _pvcData.value = PVCState(isLoading = true)
            }
            is Resource.Success->{
                response.data!!.let {
                    _pvcData.value = PVCState(data = it)
                }
            }
            is Resource.Error->{
                _pvcData.value = PVCState(error = "Somthing won't wrong.")
            }
        }
    }

    fun setWeekOf(weekOf: WeekOffModel) = viewModelScope.launch {
        _weekOfData.value = WeekOfState(isLoading = true)
        val response  =withContext(Dispatchers.IO){
            repositoryImpl.setWeekOff(weekOf)
        }
        when(response){
            is Resource.Loading->{
                _weekOfData.value = WeekOfState(isLoading = true)
            }
            is Resource.Success->{
                response.data!!.let {
                    _weekOfData.value = WeekOfState(data = it)
                }
            }
            is Resource.Error->{
                _weekOfData.value = WeekOfState(error = "Somthing won't wrong.")
            }
        }

    }

    fun setPvc(pvc: PvcExpiryDateResetModel) = viewModelScope.launch {
        _pvcData.value = PVCState(isLoading = true)
        val response = withContext(Dispatchers.IO){ repositoryImpl.setPvcExpiryDateReset(pvc) }
        when(response){
            is Resource.Loading->{
                _pvcData.value = PVCState(isLoading = true)
            }
            is Resource.Success->{
                response.data!!.let {
                    _pvcData.value = PVCState(data = it)
                }
            }
            is Resource.Error->{
                _pvcData.value = PVCState(error = "Somthing won't wrong.")
            }
        }
    }

}