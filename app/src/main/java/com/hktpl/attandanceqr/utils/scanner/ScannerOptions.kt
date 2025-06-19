package com.hktpl.attandanceqr.utils.scanner

import android.content.Context
import android.media.SoundPool
import androidx.camera.core.Camera
import com.hktpl.attandanceqr.R


open class ScannerOptions(val camera: Camera? = null): CameraInfos{
    private var soundId: Int = 0
    override fun cameraTorch(boolean: Boolean) {
        if (camera!!.cameraInfo.hasFlashUnit()){
            camera.cameraControl.enableTorch(boolean)
        }
    }

    override fun cameraBeep(boolean: Boolean, context: Context) {
        val soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundId = soundPool.load(context, R.raw.beep, 1)
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

}