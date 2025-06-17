package com.hktpl.attandanceqr.ui.main

import androidx.lifecycle.ViewModel
import com.hktpl.attandanceqr.di.repo.RestRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repositoryImpl: RestRepositoryImpl) : ViewModel(){
}