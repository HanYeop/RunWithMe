package com.ssafy.runwithme.view.crew_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewBoardSummaryBinding
import com.ssafy.runwithme.model.response.CrewBoardResponse

class CrewDetailBoardsTop3Adapter (private val listener: CrewBoardsListener) : ListAdapter<CrewBoardResponse, CrewDetailBoardsTop3Adapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCrewBoardSummaryBinding): RecyclerView.ViewHolder(binding.root){

        init{
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(crewBoardResponse: CrewBoardResponse){
            binding.crewBoardResponse = crewBoardResponse
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCrewBoardSummaryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<CrewBoardResponse>(){
            override fun areItemsTheSame(oldItem: CrewBoardResponse, newItem: CrewBoardResponse): Boolean {
                return oldItem.crewBoardDto.crewBoardSeq == newItem.crewBoardDto.crewBoardSeq
            }

            override fun areContentsTheSame(oldItem: CrewBoardResponse, newItem: CrewBoardResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}