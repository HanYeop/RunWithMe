package com.ssafy.runwithme.view.my_page.tab.my_board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyBoardBinding
import com.ssafy.runwithme.model.response.CrewBoardResponse

class MyBoardAdapter(private val deleteDialogListener: DeleteDialogListener) : PagingDataAdapter<CrewBoardResponse, MyBoardAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMyBoardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                imageDeleteMyBoard.setOnClickListener {
                    deleteDialogListener.onItemClick(getItem(adapterPosition)!!.crewBoardDto.crewBoardSeq)
                }
            }
        }

        fun bind(myBoard: CrewBoardResponse) {
            binding.apply {
                if(myBoard.imageFileDto.imgSeq == 0){
                    imageBoard.visibility = View.GONE
                }
                board = myBoard
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CrewBoardResponse, newItem: CrewBoardResponse) =
                oldItem.crewBoardDto.crewBoardSeq == newItem.crewBoardDto.crewBoardSeq
        }
    }
}