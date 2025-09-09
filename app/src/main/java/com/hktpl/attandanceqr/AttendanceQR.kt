package com.hktpl.attandanceqr

import com.hktpl.attandanceqr.BuildConfig
import android.app.Application
import android.os.StrictMode
import com.google.android.material.color.DynamicColors
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.hktpl.attandanceqr.utils.internet.ConnectivityReceiver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AttendanceQR: Application(){
    override fun onCreate() {
        super.onCreate()
        // Apply wallpaper-based color theming
        DynamicColors.applyToActivitiesIfAvailable(this)
        ConnectivityReceiver()
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
    }
}