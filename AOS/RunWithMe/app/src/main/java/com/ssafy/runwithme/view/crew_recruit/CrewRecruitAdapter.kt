package com.ssafy.runwithme.view.crew_recruit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewRecruitBinding
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.view.home.my_crew.MyCurrentCrewAdapter

class CrewRecruitAdapter(private val listener : CrewRecruitListener) : PagingDataAdapter<RecruitCrewResponse, CrewRecruitAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCrewRecruitBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition)!!)
            }
        }

        fun bind(crew: RecruitCrewResponse){
            binding.crew = crew
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCrewRecruitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null){
            holder.bind(currentItem)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RecruitCrewResponse>() {
            override fun areItemsTheSame(oldItem: RecruitCrewResponse, newItem: RecruitCrewResponse) =
                oldItem.crewDto.crewSeq == newItem.crewDto.crewSeq

            override fun areContentsTheSame(
                oldItem: RecruitCrewResponse,
                newItem: RecruitCrewResponse
            ) =
                oldItem == newItem
        }
    }


}