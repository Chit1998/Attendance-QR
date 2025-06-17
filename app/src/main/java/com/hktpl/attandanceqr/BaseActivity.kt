package com.hktpl.attandanceqr

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hktpl.attandanceqr.internet.InternetConnection
import com.hktpl.attandanceqr.internet.InternetConnectionCallBack
import com.hktpl.attandanceqr.internet.objects.InternetConnectionObserver

open class BaseActivity : AppCompatActivity() {

    var internetStatus: Boolean = false
    private lateinit var connection: InternetConnection

    companion object{
        const val TAG = "baseActivity"
    }

    override fun onResume() {
        super.onResume()
        checkInternetConnection()
    }
    
    private fun checkInternetConnection() {
        connection = InternetConnection(this)
        connection.observe(this){ isConnected ->
            if (!isConnected) {
                internetStatus = isConnected
            }else {
                internetStatus = isConnected
            }
            Log.d(TAG, "checkInternetConnection: $internetStatus")
        }
    }

    private fun openInternetDialog() {
        val view = layoutInflater.inflate(R.layout.internet_dialog, null)
        val builder = AlertDialog.Builder(this@BaseActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val txt = view.findViewById<TextView>(R.id.btnOK)
        dialog.setCancelable(false)
        txt.setOnClickListener {
            dialog.cancel()
            dialog.dismiss()
        }
        dialog.show()
    }

}