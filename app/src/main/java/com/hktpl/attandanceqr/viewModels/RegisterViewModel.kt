package com.hktpl.attandanceqr.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.RegisterUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {

    private val userData = MutableLiveData<RegisterUserModel.UserInfoModel?>()

    val user : MutableLiveData<RegisterUserModel.UserInfoModel?>
        get() = userData
    fun registerUser(user: RegisterUserModel) = viewModelScope.launch {
        try {
            repositoryImpl.registerUser(user).let { response ->
                if (response.isSuccessful){
                    userData.value = response.body()
                }else {
                    userData.postValue(null)
                }
            }
        }catch (e: Exception){
            userData.postValue(null)
        }
    }
}