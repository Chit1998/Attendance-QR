package com.hktpl.attandanceqr.ui

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityScannerBinding
import com.hktpl.attandanceqr.ui.main.MainActivity.Companion.obj
import com.hktpl.attandanceqr.utils.scanner.ScannerOptions
import java.util.Timer
import java.util.TimerTask

class ScannerActivity : BaseActivity() {
    private lateinit var binding: ActivityScannerBinding
    private lateinit var scannerOptions: ScannerOptions
    private lateinit var camera: Camera
    private lateinit var timer: Timer
    private val scanner by lazy {
        BarcodeScanning.getClient()
    }
    private var rawValue: String? = ""
    var flash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scannerOptions = ScannerOptions()
        startCamera()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                finish()
            }
        },50000)
    }

    override fun onPause() {
        super.onPause()
        if (rawValue!!.isEmpty()){
            obj?.qrResults("")
            timer.cancel()
            finish()
            return
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.preview.surfaceProvider
            }
            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    if (internetStatus == true){
                                        if (rawValue!!.isEmpty()){
                                            rawValue = barcode.rawValue
                                            obj?.qrResults(rawValue)
                                            scannerOptions.cameraBeep(true, this)
                                            finish()
                                        }
                                    }else {
                                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                                    }
                                    // Do something with the QR code
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            scannerOptions = ScannerOptions(camera)
            binding.imgFlash.setOnClickListener {
                if (flash == false){
                    flash = true
                    scannerOptions.cameraTorch(true)
                    binding.imgFlash.setImageResource(R.drawable.flash_off_rounded)
                }else{
                    flash = false
                    scannerOptions.cameraTorch(false)
                    binding.imgFlash.setImageResource(R.drawable.flash_on_rounded)
                }
            }
        }, ContextCompat.getMainExecutor(this))
    }

}