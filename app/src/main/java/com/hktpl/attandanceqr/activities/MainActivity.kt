package com.hktpl.attandanceqr.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var myLongitude: Double = 0.0
    var myLatitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.historyFragment
        ))
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    return@setOnItemSelectedListener true
                }R.id.history -> {
                    navController.navigate(R.id.historyFragment)
                    return@setOnItemSelectedListener true
                }else ->{
                navController.navigate(R.id.homeFragment)
                return@setOnItemSelectedListener false
            }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val id = binding.bottomNav.selectedItemId
        if (!navController.navigateUp()){
            if (R.id.home != id) {
                navController.navigate(R.id.homeFragment)
                binding.bottomNav.menu.getItem(0).setChecked(true)
            }else {
                finish()
            }
        }else {
            navController.navigateUp()
        }
    }
}