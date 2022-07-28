package com.ssafy.runwithme.view.my_page.tab.my_board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyBoardBinding
import com.ssafy.runwithme.model.response.MyTotalBoardsResponse

class MyBoardAdapter : PagingDataAdapter<MyTotalBoardsResponse, MyBoardAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMyBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(board: MyTotalBoardsResponse) {
            binding.board = board
            binding.executePendingBindings()
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
        private val diffUtil = object : DiffUtil.ItemCallback<MyTotalBoardsResponse>() {
            override fun areItemsTheSame(oldItem: MyTotalBoardsResponse, newItem: MyTotalBoardsResponse) =
                oldItem.crewBoardSeq == newItem.crewBoardSeq

            override fun areContentsTheSame(
                oldItem: MyTotalBoardsResponse,
                newItem: MyTotalBoardsResponse
            ) =
                oldItem.crewBoardContent == newItem.crewBoardContent
        }
    }
}