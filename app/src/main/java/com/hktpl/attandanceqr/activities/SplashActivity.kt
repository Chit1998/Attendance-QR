package com.hktpl.attandanceqr.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivitySplashBinding
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.viewModels.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewmodel: RoleViewModel by viewModels()
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
            if (preferences.getUserId() != null){
                viewmodel.getRole(UserModel(preferences.getOid()))
                viewmodel.role.observe(this){ res ->
                    if (res != null){
                        preferences.setRole(res.message!!)
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }else {
                        Toast.makeText(this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        },1000)


    }


}