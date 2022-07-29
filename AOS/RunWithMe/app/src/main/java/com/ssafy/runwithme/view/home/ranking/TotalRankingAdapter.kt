package com.ssafy.runwithme.view.home.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemTotalUserRankingBinding
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.view.home.HomeViewModel

class TotalRankingAdapter(private val homeViewModel : HomeViewModel) : ListAdapter<RankingResponse, TotalRankingAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemTotalUserRankingBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(totalRank: RankingResponse){
            binding.homeVM = homeViewModel
            binding.totalRank = totalRank
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTotalUserRankingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<RankingResponse>(){
            override fun areItemsTheSame(oldItem: RankingResponse, newItem: RankingResponse): Boolean {
                return oldItem.userSeq == newItem.userSeq
            }

            override fun areContentsTheSame(oldItem: RankingResponse, newItem: RankingResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}