package com.hktpl.attandanceqr.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hktpl.attandanceqr.adapter.AttendanceAdapter
import com.hktpl.attandanceqr.databinding.FragmentHistoryBinding
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.peferences.UserPreferences
import com.hktpl.attandanceqr.viewModels.AttendanceViewModel
import com.hktpl.attandanceqr.viewModels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var preferences: UserPreferences
    private val viewmodel: AttendanceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        preferences = UserPreferences(requireContext())
        binding.swipeRefreshLayoutAllAttendance.setOnRefreshListener { getAttendance() }
        getAttendance()
        binding.txtAppVersion.text = "Version Code: ${requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName}"
        return binding.root
    }

    private fun getAttendance(): Unit {
        binding.swipeRefreshLayoutAllAttendance.isRefreshing = true
        viewmodel.getAttendanceListApi(UserModel(preferences.getOid()))
        viewmodel.attendanceList.observe(viewLifecycleOwner){ res ->
            if (res != null){
                if (res.size > 0){
                    val adapter = AttendanceAdapter(res)
                    binding.recyclerViewAllAttendance.adapter = adapter
                    binding.swipeRefreshLayoutAllAttendance.isRefreshing = false
                    adapter.notifyDataSetChanged()
                }else {
                    binding.swipeRefreshLayoutAllAttendance.isRefreshing = false
                }
            }else {
                binding.swipeRefreshLayoutAllAttendance.isRefreshing = false
            }
        }
    }

}