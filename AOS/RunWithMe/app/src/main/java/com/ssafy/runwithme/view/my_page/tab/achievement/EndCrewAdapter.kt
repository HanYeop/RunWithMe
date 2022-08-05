package com.ssafy.runwithme.view.my_page.tab.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyCrewHistoryBinding
import com.ssafy.runwithme.model.dto.EndCrewFileDto

class EndCrewAdapter(private val listener: EndCrewListener) : ListAdapter<EndCrewFileDto, EndCrewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMyCrewHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cardMyCrewHistory.setOnClickListener {
                    listener.onItemClick(getItem(adapterPosition)!!.crewDto, getItem(adapterPosition)!!.imageFileDto)
                }
            }
        }

        fun bind(endCrew: EndCrewFileDto) {
            binding.endCrew = endCrew
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyCrewHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<EndCrewFileDto>() {
            override fun areItemsTheSame(oldItem: EndCrewFileDto, newItem: EndCrewFileDto) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: EndCrewFileDto, newItem: EndCrewFileDto) =
                oldItem.crewDto.crewSeq == newItem.crewDto.crewSeq
        }
    }
}