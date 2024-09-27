package com.hktpl.attandanceqr

import androidx.fragment.app.Fragment
import com.hktpl.attandanceqr.internet.InternetConnection

open class BaseFragment : Fragment() {
    var internetStatus: Boolean = false
    private lateinit var connection: InternetConnection

    override fun onResume() {
        super.onResume()
        checkInternetConnection()
    }
    private fun checkInternetConnection() {
        connection = InternetConnection(requireContext())
        connection.observe(viewLifecycleOwner){ isConnected ->
            if (!isConnected) {
                internetStatus = false
            }else {
                internetStatus = true
            }
        }
    }
}