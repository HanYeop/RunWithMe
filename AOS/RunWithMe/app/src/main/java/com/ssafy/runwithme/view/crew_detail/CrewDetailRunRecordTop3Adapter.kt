package com.ssafy.runwithme.view.crew_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemRunningRecordBinding
import com.ssafy.runwithme.model.dto.RunRecordDto

class CrewDetailRunRecordTop3Adapter(private val listener: CrewRunRecordListener) : ListAdapter<RunRecordDto, CrewDetailRunRecordTop3Adapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRunningRecordBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(crewRunRecord: RunRecordDto){
            Log.d("test5", "bind: $crewRunRecord")
            binding.runningRecord = crewRunRecord
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRunningRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<RunRecordDto>(){
            override fun areItemsTheSame(oldItem: RunRecordDto, newItem: RunRecordDto): Boolean {
                return oldItem.runRecordSeq == newItem.runRecordSeq
            }

            override fun areContentsTheSame(oldItem: RunRecordDto, newItem: RunRecordDto): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}