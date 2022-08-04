package com.ssafy.runwithme.view.crew_detail.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewBoardBinding
import com.ssafy.runwithme.model.dto.MyUserDto
import com.ssafy.runwithme.model.response.CrewBoardResponse

class CrewBoardAdapter(private val crewBoardListener: CrewBoardListener, private val userSeq: Int) : PagingDataAdapter<CrewBoardResponse, CrewBoardAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemCrewBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                imgDelete.setOnClickListener {
                    crewBoardListener.onDeleteClick(getItem(adapterPosition)!!)
                }
                imgReport.setOnClickListener {
                    crewBoardListener.onReportClick(getItem(adapterPosition)!!.crewBoardDto.crewBoardSeq)
                }
            }
        }

        fun bind(board: CrewBoardResponse) {
            binding.board = board
            binding.userDto = MyUserDto(userSeq)
            binding.executePendingBindings()
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
                oldItem.crewBoardDto.crewBoardSeq == newItem.crewBoardDto.crewBoardSeq

            override fun areContentsTheSame(
                oldItem: CrewBoardResponse,
                newItem: CrewBoardResponse
            ) =
                oldItem == newItem
        }
    }
}