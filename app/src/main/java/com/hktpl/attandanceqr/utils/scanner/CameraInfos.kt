package com.hktpl.attandanceqr.utils.scanner

import android.content.Context

interface CameraInfos {
    fun cameraTorch(boolean: Boolean)
    fun cameraBeep(boolean: Boolean, context: Context)
}
