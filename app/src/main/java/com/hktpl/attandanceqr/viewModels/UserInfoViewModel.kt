package com.hktpl.attandanceqr.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {
    private val userData = MutableLiveData<UserModel.EmployeeInfoModel?>()

    val user : MutableLiveData<UserModel.EmployeeInfoModel?>
        get() = userData

    fun userInfo(user: UserModel) = viewModelScope.launch {
        try{
            repositoryImpl.getUserInfo(user).let { response ->
                if (response.isSuccessful) {
                    userData.value = response.body()
                } else {
                    userData.postValue(null)
                }
            }
        }catch (e: Exception){
            userData.postValue(null)
        }
    }
}