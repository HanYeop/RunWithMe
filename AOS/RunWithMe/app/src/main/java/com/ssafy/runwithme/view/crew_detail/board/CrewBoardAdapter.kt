package com.ssafy.runwithme.view.crew_detail.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewBoardBinding
import com.ssafy.runwithme.model.response.CrewBoardResponse

class CrewBoardAdapter : PagingDataAdapter<CrewBoardResponse, CrewBoardAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCrewBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // TODO : 클릭 리스너
        init {

        }

        // TODO : 바인딩
        fun bind(board: CrewBoardResponse) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCrewBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CrewBoardResponse>() {
            override fun areItemsTheSame(oldItem: CrewBoardResponse, newItem: CrewBoardResponse) =
                oldItem.content == newItem.content

            override fun areContentsTheSame(
                oldItem: CrewBoardResponse,
                newItem: CrewBoardResponse
            ) =
                oldItem == newItem
        }
    }
}