package com.ssafy.runwithme.view.crew_detail.my_record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyRunningRecordBinding
import com.ssafy.runwithme.databinding.ItemRunningRecordBinding
import com.ssafy.runwithme.model.dto.RunRecordDto

class CrewMyRunRecordAdapter : RecyclerView.Adapter<CrewMyRunRecordAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemMyRunningRecordBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(runRecord: RunRecordDto) {
            binding.myRunRecord = runRecord
            binding.executePendingBindings()
        }
    }

    val dayRecord = mutableListOf<RunRecordDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyRunningRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayRecord[position])
    }

    override fun getItemCount(): Int = dayRecord.size
}