package com.ssafy.runwithme.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.base.BaseResponse
import com.ssafy.runwithme.model.response.MyCurrentCrewResponse
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.view.home.my_crew.MyCurrentCrewAdapter
import com.ssafy.runwithme.view.home.ranking.TotalRankingAdapter
import com.ssafy.runwithme.view.running.list.RunningListAdapter

object RecyclerViewBinding {

    // 페이징 하지 않는 리사이클러뷰 아이템 바인딩
    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, result: Result<*>) {
        if (result is Result.Success) {
            if(result.data is BaseResponse<*>) {
                when (view.adapter) {
                    is MyCurrentCrewAdapter -> {
                        (view.adapter as ListAdapter<Any, *>).submitList(result.data.data as List<MyCurrentCrewResponse>)
                    }
                    is TotalRankingAdapter -> {
                        (view.adapter as ListAdapter<Any, *>).submitList(result.data.data as List<RankingResponse>)
                    }
                    is RunningListAdapter -> {
                        (view.adapter as ListAdapter<Any, *>).submitList(result.data.data as List<MyCurrentCrewResponse>)
                    }
                    // 같은 형태로 추가하면 됨
                }
            }
        } else if (result is Result.Empty) {
            (view.adapter as ListAdapter<Any, *>).submitList(emptyList())
        }
    }
}