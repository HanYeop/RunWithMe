package com.ssafy.runwithme.view.competition.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCompetitionRankingBinding
import com.ssafy.runwithme.model.response.RankingResponse

class CompetitionRankingAdapter(private val listener: CompetitionRankingListener) : ListAdapter<RankingResponse, CompetitionRankingAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemCompetitionRankingBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(totalRank: RankingResponse){
            binding.totalRank = totalRank
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition).userSeq)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompetitionRankingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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