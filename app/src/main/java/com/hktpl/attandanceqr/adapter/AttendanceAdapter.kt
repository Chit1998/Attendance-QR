package com.hktpl.attandanceqr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hktpl.attandanceqr.databinding.RvAttendanceViewLayoutBinding
import com.hktpl.attandanceqr.models.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AttendanceAdapter(
    private val list: MutableList<UserModel.AttendanceDataModel>
) : Adapter<AttendanceAdapter.AttendanceViewHolder>(){
    inner class AttendanceViewHolder(val binding: RvAttendanceViewLayoutBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(
            RvAttendanceViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(this.inTime!!))
                val inTime = SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(this.inTime))

                val outTime: String = if (this.outTime != null && this.outTime > 0) {
                    val date = Date(this.outTime)
                    val dtFormater = SimpleDateFormat("hh:mm aa")
                    dtFormater.format(date)
                } else {
                    ""
                }
                binding.textViewDate.text = date
                binding.textViewInTimeShow.text = inTime
                binding.textViewOutTimeShow.text = outTime
                binding.textViewInLocationShow.text = this.inSite
                binding.textViewOutLocationShow.text = this.outSite
            }
        }
    }
}