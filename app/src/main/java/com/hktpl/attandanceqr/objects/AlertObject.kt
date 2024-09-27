package com.hktpl.attandanceqr.objects

import android.app.AlertDialog
import android.content.Context

object AlertObject {
    fun showAlert(context: Context?, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        // Add a positive button with a click listener
        alertDialog.setPositiveButton("OK") { _, _ ->
            // Do nothing
        }

        alertDialog.show()
    }
}