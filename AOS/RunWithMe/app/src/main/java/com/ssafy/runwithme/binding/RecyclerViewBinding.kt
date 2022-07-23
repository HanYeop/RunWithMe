package com.ssafy.runwithme.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.model.dto.MyCurrentCrewResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.view.home.HomeMyCurrentCrewAdapter
import com.ssafy.runwithme.view.home.my_crew.MyCurrentCrewAdapter

object RecyclerViewBinding {

    // 검색 리사이클러뷰 아이템 바인딩
    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, result: Result<*>) {
        if (result is Result.Success) {
            when (view.adapter) {
                is HomeMyCurrentCrewAdapter -> {
                    (view.adapter as ListAdapter<Any, *>).submitList(result.data as List<MyCurrentCrewResponse>)
                }
                is MyCurrentCrewAdapter -> {
                    (view.adapter as ListAdapter<Any, *>).submitList(result.data as List<MyCurrentCrewResponse>)
                }
            }
        } else if (result is Result.Empty) {
            (view.adapter as ListAdapter<Any, *>).submitList(emptyList())
        }
    }
}