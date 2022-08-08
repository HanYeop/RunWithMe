package com.ssafy.runwithme.view.my_page.recommend_scrap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyScrapBinding
import com.ssafy.runwithme.model.dto.ScrapInfoDto

class MyRecommendScrapAdapter(private val listener : MyRecommendScrapListener) : ListAdapter<ScrapInfoDto, MyRecommendScrapAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(val binding: ItemMyScrapBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.apply {
                this.root.setOnClickListener {
                    // listener.onItemClick(getItem(adapterPosition)!!)
                }
            }
        }

        fun bind(scrapInfoDto: ScrapInfoDto) {
            binding. = runRecord
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyScrapBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<ScrapInfoDto>(){
            override fun areItemsTheSame(oldItem: ScrapInfoDto, newItem: ScrapInfoDto): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ScrapInfoDto, newItem: ScrapInfoDto): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}