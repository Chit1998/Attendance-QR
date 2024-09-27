package com.hktpl.attandanceqr.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.models.WeekOffResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeekOffViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {
    private val weekOffData = MutableLiveData<WeekOffResponseModel?>()

    val weekOff : MutableLiveData<WeekOffResponseModel?>
        get() = weekOffData

    fun getWeekOff(model: ShowWeekOffModel) = viewModelScope.launch {
        try {
            repositoryImpl.getWeekOf(model).let { response ->
                if (response.isSuccessful) {
                    weekOffData.value = response.body()
                }else {
                    weekOffData.postValue(null)
                }
            }
        }catch (e: Exception){
            weekOffData.postValue(null)
        }
    }

    fun setWeekOff(model: WeekOffModel) = viewModelScope.launch {
        try {
            repositoryImpl.setWeekOff(model).let { response ->
                if (response.isSuccessful) {
                    weekOffData.value = response.body()
                }else {
                    weekOffData.postValue(null)
                }
            }
        }catch (e: Exception){
            weekOffData.postValue(null)
        }
    }
}