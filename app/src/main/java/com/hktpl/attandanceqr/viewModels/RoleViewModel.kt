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
class RoleViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {
    private val roleData = MutableLiveData<UserModel.MsgModel?>()

    val role : MutableLiveData<UserModel.MsgModel?>
        get() = roleData
    fun getRole(user: UserModel) = viewModelScope.launch {
        try {
            repositoryImpl.getRole(user).let { response ->
                if (response.isSuccessful){
                    roleData.value = response.body()
                }else {
                    roleData.postValue(null)
                }
            }
        }catch (e: Exception){
            roleData.postValue(null)
        }
    }
}