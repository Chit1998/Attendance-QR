package com.hktpl.attandanceqr.utils.internet

interface ConnectivityListener {
    fun onNetworkConnectionChanged(isConnected: Boolean)
}