package com.hktpl.attandanceqr

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.hktpl.attandanceqr.utils.internet.ConnectivityListener
import com.hktpl.attandanceqr.utils.internet.ConnectivityReceiver

open class BaseActivity : AppCompatActivity(), ConnectivityListener {

    var internetStatus: Boolean = false
    var dialog: AlertDialog? = null
    var appVersion = ""
    companion object{
        const val TAG = "baseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        appVersion = "App Version ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityListener = this
        dialog = openInternetDialog()
    }

    private fun openInternetDialog(): AlertDialog {
        val view = layoutInflater.inflate(R.layout.internet_dialog, null)
        val builder = AlertDialog.Builder(this@BaseActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val txt = view.findViewById<TextView>(R.id.btnOK)
        dialog.setCancelable(false)
        txt.setOnClickListener {
            dialog.cancel()
            dialog.dismiss()
        }
        return dialog
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        internetStatus = isConnected
    }
}