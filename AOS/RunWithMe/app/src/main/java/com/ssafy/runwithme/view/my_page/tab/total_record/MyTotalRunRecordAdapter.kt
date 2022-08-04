package com.ssafy.runwithme.view.my_page.tab.total_record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyRunningRecordBinding
import com.ssafy.runwithme.databinding.ItemRunningRecordBinding
import com.ssafy.runwithme.model.dto.RunRecordDto

class MyTotalRunRecordAdapter(private val listener : MyTotalRunRecordListener) : ListAdapter<RunRecordDto, MyTotalRunRecordAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(val binding: ItemMyRunningRecordBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                this.root.setOnClickListener {
                    listener.onItemClick(getItem(adapterPosition)!!)
                }
            }
        }

        fun bind(runRecord: RunRecordDto) {
            binding.myRunRecord = runRecord
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyRunningRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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