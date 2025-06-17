package com.hktpl.attandanceqr.ui.register

import androidx.lifecycle.ViewModel
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){
}