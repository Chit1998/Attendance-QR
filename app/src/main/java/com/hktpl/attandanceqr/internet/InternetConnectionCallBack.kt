package com.hktpl.attandanceqr.internet

interface InternetConnectionCallBack {
    fun onConnected(connection: Boolean)
    fun onDisconnected(connection: Boolean)
}