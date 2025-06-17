package com.hktpl.attandanceqr.ui.register

import android.os.Bundle
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityRegisterBinding
import com.hktpl.attandanceqr.peferences.UserPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
//    private val viewmodel: RegisterViewModel by viewModels()
//    private val roleViewModel: RoleViewModel by viewModels()
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
//                viewmodel.registerUser(RegisterUserModel(binding.eUsername.text.toString().trim()))
//                viewmodel.user.observe(this){ res ->
//                    if (res != null){
//                        if (res.message == FOUND){
//                            preferences.setOid(res.oid)
//                            preferences.setData(res.empId, res.name)
//                            getRole(res.oid)
//                        }else {
//                            Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }else {
//                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        }
        binding.txtAppVersion.text = "Version Code: ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }

//    private fun getRole(oid: String) {
//        roleViewModel.getRole(UserModel(oid))
//        roleViewModel.role.observe(this){ res ->
//            if (res != null){
//                preferences.setRole(res.message!!)
//                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
//                finish()
//            }else {
//                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}