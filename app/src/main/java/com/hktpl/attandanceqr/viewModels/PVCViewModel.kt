package com.hktpl.attandanceqr.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.PvcExpiryDateResetResponseModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PVCViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel() {

    private val pvcData = MutableLiveData<PvcExpiryDateResetResponseModel?>()

    val pvc : MutableLiveData<PvcExpiryDateResetResponseModel?>
        get() = pvcData

    fun getPvc(model: ShowWeekOffModel) = viewModelScope.launch {
        try {
            repositoryImpl.getPvcExpiryDate(model).let { response ->
                if (response.isSuccessful) {
                    pvcData.value = response.body()
                    Log.d("TAG_DATA", "getPvc:1 ${response.body()!!.message}")
                }else {
                    pvcData.postValue(null)
                }
            }
        }catch (e: Exception){
            pvcData.postValue(null)
        }
    }

    fun setPvc(model: PvcExpiryDateResetModel) = viewModelScope.launch {
        try {
            repositoryImpl.setPvcExpiryDateReset(model).let { response ->
                if (response.isSuccessful) {
                    pvcData.value = response.body()
                }else {
                    pvcData.postValue(null)
                }
            }
        }catch (e: Exception){
            pvcData.postValue(null)
        }
    }

}