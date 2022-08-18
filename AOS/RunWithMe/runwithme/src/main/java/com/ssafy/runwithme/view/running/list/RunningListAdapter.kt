package com.ssafy.runwithme.view.running.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemMyCurrentCrewBinding
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse

class RunningListAdapter (private val listener: RunningListListener) : RecyclerView.Adapter<RunningListAdapter.ViewHolder>() {

    private var myCrewList = emptyList<MyCurrentCrewResponse>()

    inner class ViewHolder(private val binding: ItemMyCurrentCrewBinding) : RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener {
                listener.onItemClick(myCrewList.get(position))
            }
        }

        fun bind(item: MyCurrentCrewResponse){
                binding.myCurrentCrewResponse = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyCurrentCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myCrewList.get(position))
    }

    override fun getItemCount(): Int = myCrewList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList : List<MyCurrentCrewResponse>){
        myCrewList = newList
        notifyDataSetChanged()
        Log.d("test5", "setData: $myCrewList")
    }
}

//class RunningListAdapter(private val listener: RunningListListener) : ListAdapter<MyCurrentCrewResponse, RunningListAdapter.ViewHolder>(
//    diffUtil
//){
//
//    inner class ViewHolder(private val binding: ItemMyCurrentCrewBinding): RecyclerView.ViewHolder(binding.root){
//
//        init {
//            binding.root.setOnClickListener {
//                listener.onItemClick(getItem(adapterPosition))
//            }
//        }
//        fun bind(myCurrentCrew: MyCurrentCrewResponse){
//            binding.myCurrentCrewResponse = myCurrentCrew
//            binding.executePendingBindings()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemMyCurrentCrewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    companion object{
//        val diffUtil = object : DiffUtil.ItemCallback<MyCurrentCrewResponse>(){
//            override fun areItemsTheSame(oldItem: MyCurrentCrewResponse, newItem: MyCurrentCrewResponse): Boolean {
//                return oldItem.crewDto.crewSeq == newItem.crewDto.crewSeq
//            }
//
//            override fun areContentsTheSame(oldItem: MyCurrentCrewResponse, newItem: MyCurrentCrewResponse): Boolean {
//                return oldItem.hashCode() == newItem.hashCode()
//            }
//        }
//    }
//}