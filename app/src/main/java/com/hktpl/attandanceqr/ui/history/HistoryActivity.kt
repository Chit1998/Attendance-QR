package com.hktpl.attandanceqr.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.adapter.AttendanceAdapter
import com.hktpl.attandanceqr.databinding.ActivityHistoryBinding
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.peferences.UserPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewmodel: HistoryViewModel by viewModels()
    private lateinit var preferences: UserPreferences
    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolBar.txtToolbarTitle.text = getString(R.string.attendance_history)
        binding.txtAppVersion.text = appVersion
        preferences = UserPreferences(this)
        binding.swipeRefreshLayoutAllAttendance.setOnRefreshListener {
            attendances()
        }
        attendances()
    }
    private fun attendances() {
        binding.swipeRefreshLayoutAllAttendance.isRefreshing = true
        viewmodel.getAttendance(UserModel(preferences.getOid()))
        viewmodel.historyData.observe(this) { response ->
            if (response != null){
                if (response.isLoading){
                    binding.progressBarHistory.visibility = VISIBLE
                    binding.progressBarHistory.progress
                }
                if (response.error!!.isNotEmpty()){
                    binding.progressBarHistory.visibility = GONE
                    binding.swipeRefreshLayoutAllAttendance.isRefreshing = false
                    if (internetStatus){
                        number = 0
                        Toast.makeText(this, "${viewmodel.historyData.value?.error}", Toast.LENGTH_SHORT).show()
                    }else{
                        attendances()
                        if (number == 0){
                            number = 1
                            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (response.data != null) {
                    binding.swipeRefreshLayoutAllAttendance.isRefreshing = false
                    binding.progressBarHistory.visibility = GONE
                    if (response.data.isEmpty()){
                        binding.txtNoData.visibility = VISIBLE
                        binding.recyclerViewAllAttendance.visibility = GONE
                    }else{
                        val adapter = AttendanceAdapter(response.data)
                        binding.recyclerViewAllAttendance.adapter = adapter
                        binding.recyclerViewAllAttendance.visibility = VISIBLE
                        binding.txtNoData.visibility = GONE
                    }
                }
            }
        }
    }
}
