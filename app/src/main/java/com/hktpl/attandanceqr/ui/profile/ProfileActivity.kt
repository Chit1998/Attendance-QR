package com.hktpl.attandanceqr.ui.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.R
import com.hktpl.attandanceqr.databinding.ActivityProfileBinding
import com.hktpl.attandanceqr.databinding.WeekOffDialogLayoutBinding
import com.hktpl.attandanceqr.models.PvcExpiryDateResetModel
import com.hktpl.attandanceqr.models.ShowWeekOffModel
import com.hktpl.attandanceqr.models.UserModel
import com.hktpl.attandanceqr.models.WeekOffModel
import com.hktpl.attandanceqr.objects.TAG.FOUND
import com.hktpl.attandanceqr.peferences.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewmodel: ProfileViewModel by viewModels()
    private lateinit var preferences: UserPreferences
    private var time = 0L
    private var number = 0
    private var weekOffStr: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.toolBar.txtToolbarTitle.text = getString(R.string.profile)
        preferences = UserPreferences(this)
        binding.btnWeekOff.setOnClickListener { openDialog(it,"weekOff") }
        binding.btnPVC.setOnClickListener { openDialog(it, "PVC") }
        binding.txtAppVersion.text = appVersion
    }

    override fun onResume() {
        super.onResume()
        userInfo()
        getWeekOf()
        getPvc()
    }

    private fun getWeekOf() {
        viewmodel.getWeekOf(ShowWeekOffModel(
            preferences.getOid()!!.toLong()
        ))
        viewmodel.weekOfData.observe(this) { response ->
            if (response != null){
                if (response.isLoading) {
                    binding.progressBarProfile.visibility = VISIBLE
                    binding.progressBarProfile.progress
                }
                if (response.error!!.isNotEmpty()) {
                    binding.progressBarProfile.visibility = GONE
                    if (internetStatus) {
                        number = 0
                        Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                    } else {
                        getWeekOf()
                        if (number == 0) {
                            number = 1
                            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (response.data != null){
                    binding.progressBarProfile.visibility = GONE
                    if (response.data.message == FOUND){
                        if (response.data.weeklyOffDay!!.isNotEmpty() || response.data.weeklyOffDay.isNotBlank()) {
                            binding.txtWeekOffValue.text = response.data.weeklyOffDay
                        }else{
                            Toast.makeText(this, getString(R.string.weekOff), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getPvc() {
        viewmodel.getPvc(ShowWeekOffModel(
            preferences.getOid()!!.toLong()
        ))
        viewmodel.pvcData.observe(this) { response ->
            if (response != null){
                if (response.isLoading) {
                    binding.progressBarProfile.visibility = VISIBLE
                    binding.progressBarProfile.progress
                }
                if (response.error!!.isNotEmpty()) {
                    binding.progressBarProfile.visibility = GONE
                    if (internetStatus) {
                        number = 0
                        Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                    } else {
                        getPvc()
                        if (number == 0) {
                            number = 1
                            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (response.data != null){
                    binding.progressBarProfile.visibility = GONE
                    if (response.data.message == FOUND){
                        if (response.data.pvcExpiryDate!! != 0L) {
                            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(response.data.pvcExpiryDate))
                            binding.txtPVCValue.text = date
                        }else{
                            Toast.makeText(this, getString(R.string.pvc), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun userInfo() {
        viewmodel.getProfileDetails(UserModel(preferences.getEmpId()))
        viewmodel.userData.observe(this){ response ->
            if (response != null){
                if (response.isLoading){
                    binding.progressBarProfile.visibility = VISIBLE
                    binding.progressBarProfile.progress
                }
                if (response.error!!.isNotEmpty()){
                    binding.progressBarProfile.visibility = GONE
                    if (internetStatus){
                        number = 0
                        Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                    }else{
                        userInfo()
                        if (number == 0){
                            number = 1
                            Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (response.data != null){
                    binding.progressBarProfile.visibility = GONE
                    binding.user = response.data
                    binding.txtName.visibility = VISIBLE
                    binding.txtNameValue.visibility = VISIBLE
                    binding.txtEmpCode.visibility = VISIBLE
                    binding.txtEmpCodeValue.visibility = VISIBLE
                if (response.data.phoneNo == null){
                    binding.txtPhoneNo.visibility = GONE
                    binding.txtPhoneNoValue.visibility = GONE
                }else {
                    binding.txtPhoneNoValue.visibility = VISIBLE
                    binding.txtPhoneNo.visibility = VISIBLE
                }
                if (response.data.email == null){
                    binding.txtEmail.visibility = GONE
                    binding.txtEmailValue.visibility = GONE
                }else {
                    binding.txtEmailValue.visibility = VISIBLE
                    binding.txtEmail.visibility = VISIBLE
                }
                if (response.data.inTime == 0L){
                    binding.txtInTime.visibility = GONE
                    binding.txtInTimeValue.visibility = GONE
                }else {
                    val inTime = SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(response.data.inTime!!))
                    binding.txtInTimeValue.visibility = VISIBLE
                    binding.txtInTime.visibility = VISIBLE
                    binding.txtInTimeValue.text = inTime
                }
                }
            }
        }
    }

    private fun openDialog(view: View, str: String) {
        val binding2 = WeekOffDialogLayoutBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(view.context)
        builder.setView(binding2.root)
        val dialog = builder.create()
        if (str == "weekOff") {
            binding2.txtSelectDate.visibility = GONE
            binding2.weekOffSpinner.visibility = VISIBLE
            binding2.txtHeadTitle.text = getString(R.string.reset_week_off)
            binding2.btnMark.text = getString(R.string.mark_week_off)
            val weekList = resources.getStringArray(R.array.weekOff)
            val adapter = ArrayAdapter(view.context, R.layout.week_off, R.id.textView, weekList)
            binding2.weekOffSpinner.adapter = adapter
            binding2.weekOffSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        weekOffStr = weekList[position].toString()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            binding2.btnMark.setOnClickListener {
                if (weekOffStr!!.isEmpty() || weekOffStr == getString(R.string.week_select)){
                    Toast.makeText(view.context, getString(R.string.select_week_of), Toast.LENGTH_SHORT).show()
                }else {
                     viewmodel.setWeekOf(WeekOffModel(
                         preferences.getOid()!!.toLong(),
                         weekOffStr.toString()
                     ))
                    viewmodel.weekOfData.observe(this) { response ->
                        if (response != null){
                            if (response.isLoading) {
                                binding.progressBarProfile.visibility = VISIBLE
                                binding.progressBarProfile.progress
                            }
                            if (response.error!!.isNotEmpty()) {
                                binding.progressBarProfile.visibility = GONE
                                if (internetStatus) {
                                    number = 0
                                    Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                                } else {
                                    getWeekOf()
                                    if (number == 0) {
                                        number = 1
                                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                dialog.cancel()
                                dialog.dismiss()
                            }
                            if (response.data != null){
                                binding.progressBarProfile.visibility = GONE
                                if (response.data.message == "Reset weekly off done"){
                                    binding.txtWeekOffValue.text = response.data.weeklyOffDay
                                    Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                }
                                dialog.cancel()
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
        else{
            binding2.btnMark.text = getString(R.string.update)
            binding2.txtHeadTitle.text = getString(R.string.police_verification)
            binding2.weekOffSpinner.visibility = GONE
            binding2.txtSelectDate.visibility = VISIBLE

            binding2.txtSelectDate.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this,
                    { view, mYear, monthOfYear, dayOfMonth ->
                        try {
                            binding2.txtSelectDate.text = "$dayOfMonth-${monthOfYear+1}-$mYear"
                            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            time = format.parse("$dayOfMonth-$monthOfYear-$mYear")?.time!!
                        }catch (e: ParseException){
                            Log.e(TAG, "onCreate: ${e.message}")
                        }
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.datePicker.minDate = Date().time-1000
                datePickerDialog.show()
            }

            binding2.btnMark.setOnClickListener {
                if (time == 0L){
                    Toast.makeText(view.context, getString(R.string.select_pvc), Toast.LENGTH_SHORT).show()
                }else {
                    viewmodel.setPvc(PvcExpiryDateResetModel(
                        preferences.getOid()!!.toLong(),
                        time
                    ))
                    viewmodel.pvcData.observe(this) { response ->
                        if (response != null){
                            if (response.isLoading) {
                                binding.progressBarProfile.visibility = VISIBLE
                                binding.progressBarProfile.progress
                            }
                            if (response.error!!.isNotEmpty()) {
                                binding.progressBarProfile.visibility = GONE
                                if (internetStatus) {
                                    number = 0
                                    Toast.makeText(this, "${viewmodel.userData.value?.error}", Toast.LENGTH_SHORT).show()
                                } else {
                                    getWeekOf()
                                    if (number == 0) {
                                        number = 1
                                        Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show()
                                    }
                                }
                                dialog.dismiss()
                                dialog.cancel()
                            }
                            if (response.data != null){
                                binding.progressBarProfile.visibility = GONE
                                if (response.data.message == FOUND){
                                    if (response.data.pvcExpiryDate!! != 0L) {
                                        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(response.data.pvcExpiryDate))
                                        binding.txtPVCValue.text = date
                                    }else{
                                        Toast.makeText(this, getString(R.string.pvc), Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                                }
                                dialog.dismiss()
                                dialog.cancel()
                            }
                        }
                    }
                }
            }
        }
        dialog.show()
    }
}