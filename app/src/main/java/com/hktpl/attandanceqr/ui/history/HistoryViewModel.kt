package com.hktpl.attandanceqr.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.objects.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl): ViewModel(){
    private val _historyData: MutableLiveData<HistoryState?> = MutableLiveData(HistoryState())

    val historyData: LiveData<HistoryState?>
        get() = _historyData

    fun getAttendance(user: UserModel) = viewModelScope.launch {
        _historyData.value = HistoryState(isLoading = true)
        val response = withContext(Dispatchers.IO){
            repositoryImpl.getAttendance(user)
        }
        when(response){
            is Resource.Loading->{
                _historyData.value = HistoryState(isLoading = true)
            }
            is Resource.Success->{
                response.data.let {
                    _historyData.value = HistoryState(data = it)
                }
            }
            is Resource.Error->{
                _historyData.value = HistoryState(error = "Somthing won't wrong.")
            }
        }
    }
}