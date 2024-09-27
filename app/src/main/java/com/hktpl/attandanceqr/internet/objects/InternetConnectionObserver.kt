package com.hktpl.attandanceqr.internet.objects

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.hktpl.attandanceqr.internet.InternetConnectionCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object InternetConnectionObserver{
    private val validNetworks: MutableSet<Network> = HashSet()
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var cm: ConnectivityManager? = null
    private var connectionCallback: InternetConnectionCallBack? = null
    fun instance(context: Context): InternetConnectionObserver {
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return this
    }
    fun setCallback(connectionCallback: InternetConnectionCallBack): InternetConnectionObserver {
        InternetConnectionObserver.connectionCallback = connectionCallback
        return this
    }

    fun register(): InternetConnectionObserver {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm?.registerNetworkCallback(networkRequest, networkCallback)
        return this
    }

    fun unRegister(){
        cm?.unregisterNetworkCallback(networkCallback)
    }
    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val networkCapabilities = cm?.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if(hasInternet){
                        withContext(Dispatchers.Main){
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }
    private fun checkValidNetworks() {
        var status = validNetworks.size > 0
        if(status){
            connectionCallback?.onConnected(connection = status)
        } else{
            connectionCallback?.onDisconnected(connection = status)
        }
    }
}