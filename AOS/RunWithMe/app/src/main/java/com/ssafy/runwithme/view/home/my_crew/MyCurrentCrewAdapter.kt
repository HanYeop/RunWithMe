package com.ssafy.runwithme.view.home.my_crew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyCurrentCrewBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse

class MyCurrentCrewAdapter(private val listener: MyCurrentCrewListener) : ListAdapter<MyCurrentCrewResponse, MyCurrentCrewAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemMyCurrentCrewBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }
            binding.imgBtnStartRunning.setOnClickListener {
                listener.onBtnStartClick(getItem(adapterPosition))
            }
        }
        fun bind(myCurrentCrew: MyCurrentCrewResponse){
            binding.myCurrentCrewResponse = myCurrentCrew
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyCurrentCrewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<MyCurrentCrewResponse>(){
            override fun areItemsTheSame(oldItem: MyCurrentCrewResponse, newItem: MyCurrentCrewResponse): Boolean {
                return oldItem.crewDto.crewSeq == newItem.crewDto.crewSeq
            }

            override fun areContentsTheSame(oldItem: MyCurrentCrewResponse, newItem: MyCurrentCrewResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}