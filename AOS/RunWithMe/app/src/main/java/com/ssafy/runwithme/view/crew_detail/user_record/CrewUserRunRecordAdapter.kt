package com.ssafy.runwithme.view.crew_detail.user_record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewBoardBinding
import com.ssafy.runwithme.databinding.ItemRunningRecordBinding
import com.ssafy.runwithme.model.dto.MyUserDto
import com.ssafy.runwithme.model.dto.RunRecordDto
import com.ssafy.runwithme.model.response.CrewBoardResponse
import com.ssafy.runwithme.view.crew_detail.board.CrewBoardAdapter
import com.ssafy.runwithme.view.crew_detail.board.CrewBoardDeleteListener

class CrewUserRunRecordAdapter (private val listener: CrewUserRunRecordListener) : PagingDataAdapter<RunRecordDto, CrewUserRunRecordAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemRunningRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
        val binding =
            ItemRunningRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RunRecordDto>() {
            override fun areItemsTheSame(oldItem: RunRecordDto, newItem: RunRecordDto) =
                oldItem.runRecordSeq == newItem.runRecordSeq

            override fun areContentsTheSame(
                oldItem: RunRecordDto,
                newItem: RunRecordDto
            ) =
                oldItem == newItem
        }
    }
}