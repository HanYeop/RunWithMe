package com.ssafy.runwithme.view.home.tab.crew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewRecruitPreviewBinding
import com.ssafy.runwithme.model.response.RecruitCrewResponse
import com.ssafy.runwithme.view.crew_recruit.CrewRecruitListener


class CrewRecruitPreviewAdapter(private val listener : CrewRecruitListener) : ListAdapter<RecruitCrewResponse, CrewRecruitPreviewAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCrewRecruitPreviewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(crew : RecruitCrewResponse){
            binding.crew = crew
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCrewRecruitPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecruitCrewResponse>(){
            override fun areItemsTheSame(oldItem: RecruitCrewResponse, newItem: RecruitCrewResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecruitCrewResponse, newItem: RecruitCrewResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}