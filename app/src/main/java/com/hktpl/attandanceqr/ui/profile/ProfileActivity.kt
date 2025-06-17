package com.hktpl.attandanceqr.ui.profile

import android.os.Bundle
import com.hktpl.attandanceqr.BaseActivity
import com.hktpl.attandanceqr.databinding.ActivityProfileBinding
import com.hktpl.attandanceqr.peferences.UserPreferences
import dagger.hilt.android.AndroidEntryPoint

//@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
//    private val viewmodel: UserInfoViewModel by viewModels()
//    private val viewmodel2: WeekOffViewModel by viewModels()
//    private val viewmodel3: PVCViewModel by viewModels()
    private lateinit var preferences: UserPreferences
    private var time = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = UserPreferences(this)
//        getUserInfo()
//        getWeekOff()
//        getPVC()

//        binding.btnWeekOff.setOnClickListener { openDialog(it,"weekOff") }
//        binding.btnPVC.setOnClickListener { openDialog(it, "PVC") }

        binding.txtAppVersion.text = "Version Code: ${packageManager.getPackageInfo(packageName, 0).versionName}"
    }

//    private fun openDialog(view: View, str: String) {
//        val binding2 = WeekOffDialogLayoutBinding.inflate(layoutInflater)
//        val builder = AlertDialog.Builder(view.context)
//        builder.setView(binding2.root)
//        val dialog = builder.create()
//        if (str == "weekOff"){
//            binding2.constraintLayoutWeekOff.visibility = VISIBLE
//            binding2.constraintLayoutPVC.visibility = GONE
//            val weekList = resources.getStringArray(R.array.weekOff)
//            val adapter = ArrayAdapter(view.context, R.layout.week_off, R.id.textView, weekList)
//            binding2.weekOffSpinner.adapter = adapter
//            var weekOffStr: String? = ""
//            binding2.weekOffSpinner.onItemSelectedListener =
//                object : AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>?,
//                        view: View?,
//                        position: Int,
//                        id: Long
//                    ) {
//                        if (position != 0) {
//                            weekOffStr = weekList[position].toString()
//                        }
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//                        TODO("Not yet implemented")
//                    }
//                }
//
//            binding2.btnMarkWeekOff.setOnClickListener {
//                if (weekOffStr == "") {
//                    Toast.makeText(
//                        view.context,
//                        getString(R.string.select_week_of),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    viewmodel2.setWeekOff(
//                        WeekOffModel(
//                            preferences.getOid()!!.toLong(),
//                            weekOffStr!!
//                        )
//                    )
//                    viewmodel2.weekOff.observe(this@ProfileActivity) { res ->
//                        if (res != null) {
//                            if (res.message == FOUND) {
//                                dialog.dismiss()
//                                dialog.cancel()
//                                Toast.makeText(
//                                    this@ProfileActivity,
//                                    getString(R.string.set_week_of),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                binding.txtWeekOffValue.text = res.weeklyOffDay
//                            } else if (res.message == "Reset weekly off done") {
//                                dialog.dismiss()
//                                dialog.cancel()
//                                Toast.makeText(
//                                    this@ProfileActivity,
//                                    getString(R.string.set_week_of),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                binding.txtWeekOffValue.text = res.weeklyOffDay
//                            }
//                        } else {
//                            dialog.dismiss()
//                            dialog.cancel()
//                            Toast.makeText(
//                                this@ProfileActivity,
//                                getString(R.string.check_connection),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//        }else{
//            binding2.constraintLayoutWeekOff.visibility = GONE
//            binding2.constraintLayoutPVC.visibility = VISIBLE
//            binding2.txtSelectDate.setOnClickListener {
//                val c = Calendar.getInstance()
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//                val datePickerDialog = DatePickerDialog(
//                    this,
//                    { view, mYear, monthOfYear, dayOfMonth ->
//                        try {
//                            binding2.txtSelectDate.text = "$dayOfMonth-$monthOfYear-$mYear"
//                            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//                            time = format.parse("$dayOfMonth-$monthOfYear-$mYear")?.time!!
//                        }catch (e: ParseException){
//                            Log.e("TAG_DATA", "onCreate: ${e.message}")
//                        }
//                    },
//                    year,
//                    month,
//                    day
//                )
//                datePickerDialog.datePicker.minDate = Date().time-1000
//                datePickerDialog.show()
//            }
//
//            binding2.btnUpdatePVC.setOnClickListener {
//                if (time != 0L){
//                    viewmodel3.setPvc(PvcExpiryDateResetModel(preferences.getOid()!!.toLong(),time))
//                    viewmodel3.pvc.observe(this){ res ->
//                        if (res != null){
//                            if (res.message == FOUND) {
//                                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(res.pvcExpiryDate!!))
//                                binding.txtPVCValue.text = date
//                                dialog.dismiss()
//                                dialog.cancel()
//                            }else{
//                                dialog.dismiss()
//                                dialog.cancel()
//                                Toast.makeText(view.context, getString(R.string.not_found), Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }else {
//                    dialog.dismiss()
//                    dialog.cancel()
//                    Toast.makeText(view.context, getString(R.string.select_pvc), Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        }
//        dialog.show()
//    }
//
//    private fun getWeekOff() {
//        viewmodel2.getWeekOff(ShowWeekOffModel(preferences.getOid()!!.toLong()))
//        viewmodel2.weekOff.observe(this){ res ->
//            if (res != null){
//                if (res.message == FOUND){
//                    binding.txtWeekOffValue.text = res.weeklyOffDay
//                }
//            }else {
//                Toast.makeText(this, getString(R.string.weekOff), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun getUserInfo() {
//        viewmodel.userInfo(UserModel(preferences.getUserId()))
//        viewmodel.user.observe(this){ res ->
//            if (res != null){
//                binding.txtEmpCodeValue.text = res.empId
//                binding.txtNameValue.text = res.name
//                if (res.phoneNo == null){
//                    binding.txtPhoneNo.visibility = GONE
//                    binding.txtPhoneNoValue.visibility = GONE
//                }else {
//                    binding.txtPhoneNoValue.visibility = VISIBLE
//                    binding.txtPhoneNo.visibility = VISIBLE
//                    binding.txtPhoneNoValue.text = res.phoneNo
//                }
//                if (res.email == null){
//                    binding.txtEmail.visibility = GONE
//                    binding.txtEmailValue.visibility = GONE
//                }else {
//                    binding.txtEmailValue.visibility = VISIBLE
//                    binding.txtEmail.visibility = VISIBLE
//                    binding.txtEmailValue.text = res.email
//                }
//                if (res.inTime == 0L){
//                    binding.txtInTime.visibility = GONE
//                    binding.txtInTimeValue.visibility = GONE
//                }else {
//                    val inTime = SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(res.inTime!!))
//                    binding.txtInTimeValue.visibility = VISIBLE
//                    binding.txtInTime.visibility = VISIBLE
//                    binding.txtInTimeValue.text = inTime
//                }
//            }else {
//                Toast.makeText(this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show()
//            }
//
//            /**
//             getUserInfo: name: Test - S/G empId: 12345 phoneNo: 1234567 date: 1721845800000
//             inTime: 1721915947000 outTime: 0 email: null markedBy: SELF message: USER FOUND
//            */
//        }
//    }
//
//    private fun getPVC() {
//        viewmodel3.getPvc(ShowWeekOffModel(preferences.getOid()!!.toLong()))
//        viewmodel3.pvc.observe(this){ res ->
//            if (res != null){
//                if (res.message == FOUND){
//                    if (res.pvcExpiryDate!! != 0L) {
//                        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(
//                            res.pvcExpiryDate
//                        ))
//                        Log.d("TAG_DATA", "getPVC: ${res.pvcExpiryDate}")
//                        binding.txtPVCValue.text = date
//                    }
//                }
//                Log.d("TAG_DATA", "getPVC: ${res.pvcExpiryDate}  res: ${res.message}")
//            }else {
//                Toast.makeText(this, getString(R.string.pvc), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}