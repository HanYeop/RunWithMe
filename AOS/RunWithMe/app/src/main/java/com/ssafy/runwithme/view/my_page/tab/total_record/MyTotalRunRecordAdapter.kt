package com.ssafy.runwithme.view.my_page.tab.total_record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemRunningRecordBinding
import com.ssafy.runwithme.model.dto.RunRecordDto

class MyTotalRunRecordAdapter : RecyclerView.Adapter<MyTotalRunRecordAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemRunningRecordBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(runRecord: RunRecordDto) {
            binding.myRunRecord = runRecord
            binding.executePendingBindings()
        }
    }

    val dayRecord = mutableListOf<RunRecordDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRunningRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dayRecord[position])
    }

    override fun getItemCount(): Int = dayRecord.size
}