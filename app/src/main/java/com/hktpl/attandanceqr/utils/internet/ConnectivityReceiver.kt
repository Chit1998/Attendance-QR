package com.hktpl.attandanceqr.utils.internet

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver : BroadcastReceiver() {

    companion object{
        const val TAG = "connectivityReceiver"
        var connectivityListener: ConnectivityListener? = null
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isConnected = isNetworkConnected(context)
//             Handle the network state change (connected or disconnected) here
            if (connectivityListener != null){
                connectivityListener?.onNetworkConnectionChanged(isConnected)
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo != null && (
                networkInfo.type == ConnectivityManager.TYPE_WIFI ||
                networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                networkInfo.type == ConnectivityManager.TYPE_ETHERNET)
                && networkInfo.isConnectedOrConnecting
    }
}