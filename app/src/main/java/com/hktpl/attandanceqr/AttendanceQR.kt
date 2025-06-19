package com.hktpl.attandanceqr

import android.app.Application
import com.hktpl.attandanceqr.utils.internet.ConnectivityReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AttendanceQR: Application(){
    override fun onCreate() {
        super.onCreate()
        ConnectivityReceiver()
    }
}