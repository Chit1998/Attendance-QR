package com.hktpl.attandanceqr.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.databinding.ActivitySplashBinding
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.ui.main.MainActivity
import com.hktpl.attandanceqr.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserPreferences(this)
        binding.txtAppVersion.text = "Version Code: ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            if (preferences.getEmpId() != null){
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }else {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            finish()
        },1000)
    }


}