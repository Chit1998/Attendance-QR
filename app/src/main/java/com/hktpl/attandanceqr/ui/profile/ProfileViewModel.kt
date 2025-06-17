package com.hktpl.attandanceqr.ui.profile

import androidx.lifecycle.ViewModel
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){
}