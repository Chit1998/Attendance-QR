package com.hktpl.attandanceqr.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityRegisterBinding
import com.hktpl.attandanceqr.models.RegisterUserModel
import com.hktpl.attandanceqr.objects.TAG.FOUND
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewmodel: RegisterViewModel by viewModels()
    private lateinit var preferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserPreferences(this)
        binding.buttonRegister.setOnClickListener {
            if (binding.eUsername.text.isEmpty()) {
                binding.eUsername.error = getString(R.string.empty_field)
            } else {
                viewmodel.registerUser(RegisterUserModel(binding.eUsername.text.toString().trim()))
                viewmodel.userData.observe(this){ res ->
                    if (res != null){
                        if (res.isLoading){
                            binding.progressBarRegister.visibility = View.VISIBLE
                            binding.progressBarRegister.progress
                        }
                        if (res.error!!.isNotEmpty()){
                            binding.progressBarRegister.visibility = View.GONE
                            if (internetStatus){
                                Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (res.data != null){
                            binding.progressBarRegister.visibility = View.GONE
                            if (res.data.message == FOUND){
                                preferences.setOid(res.data.oid)
                                preferences.setData(res.data.empId, res.data.name, res.data.phoneNo)
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }else{
                                Toast.makeText(this, res.data.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        binding.txtAppVersion.text = "Version Code: ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }

}