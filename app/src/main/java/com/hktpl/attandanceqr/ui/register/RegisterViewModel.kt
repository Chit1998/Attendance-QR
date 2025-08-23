package com.hktpl.attandanceqr.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){

    private val _userData: MutableLiveData<RegisterState?> = MutableLiveData(RegisterState())

    val userData: MutableLiveData<RegisterState?>
        get() = _userData

    fun registerUser(register: RegisterUserModel) = viewModelScope.launch {
        _userData.value = RegisterState(isLoading = true)
        repositoryImpl.registerUser(register).let { response ->
            when(response){
                is Resource.Loading->{
                    _userData.value = RegisterState(isLoading = true)
                }
                is Resource.Success->{
                    response.data!!.let {
                        _userData.value = RegisterState(data = it)
                    }
                }
                is Resource.Error->{
                        _userData.value = RegisterState(error = "Somthing won't wrong.")
                }
            }
        }
    }
}