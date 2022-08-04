package com.ssafy.runwithme.view.my_page.tab.total_record.local_run

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemPracticeRecordBinding
import com.ssafy.runwithme.model.entity.RunRecordEntity

class PracticeListAdapter() : ListAdapter<RunRecordEntity, PracticeListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemPracticeRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(runRecord: RunRecordEntity) {
            binding.myRunRecord = runRecord
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPracticeRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RunRecordEntity>() {
            override fun areItemsTheSame(oldItem: RunRecordEntity, newItem: RunRecordEntity) =
                oldItem.seq == newItem.seq

            override fun areContentsTheSame(
                oldItem: RunRecordEntity,
                newItem: RunRecordEntity
            ) =
                oldItem == newItem
        }
    }
}