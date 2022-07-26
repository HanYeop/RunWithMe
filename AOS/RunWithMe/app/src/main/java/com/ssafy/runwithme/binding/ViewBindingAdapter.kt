package com.ssafy.runwithme.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ssafy.runwithme.R
import com.ssafy.runwithme.binding.ViewBindingAdapter.setCrewHorizonImage
import com.ssafy.runwithme.utils.BASE_URL

object ViewBindingAdapter {

    @BindingAdapter("startTime", "endTime")
    @JvmStatic
    fun TextView.setStartTime(timeStart: String, timeEnd: String){
        this.text = "$timeStart ~ $timeEnd"
    }

    @BindingAdapter("startDay", "endDay")
    @JvmStatic
    fun TextView.setDay(dayStart : String, dayEnd : String){
        this.text = "$dayStart ~ $dayEnd"
    }

    @BindingAdapter("goalType", "goalAmount")
    @JvmStatic
    fun TextView.setAmount(goalType : String, goalAmount : Int){
        if(goalType == "시간"){
            this.text = "$goalAmount 분씩"
        } else {
            this.text = "$goalAmount Km"
        }
    }

    @BindingAdapter("crewHorizonImage")
    @JvmStatic
    fun ImageView.setCrewHorizonImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.ic_baseline_add_24)
                .override(R.dimen.crew_horizon_image_size,R.dimen.crew_horizon_image_size)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_horizon_image_size,R.dimen.crew_horizon_image_size)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("crewBoardProfileImage")
    @JvmStatic
    fun ImageView.setCrewBoardProfileImage (imageSeq: Any){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.user_image)
                .override(R.dimen.crew_board_profile_image_size,R.dimen.crew_board_profile_image_size)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_board_profile_image_size,R.dimen.crew_board_profile_image_size)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("crewDetailImage")
    @JvmStatic
    fun ImageView.setCrewDetailImage (imageSeq: Any){
        Glide.with(this.context)
            .load("${BASE_URL}images/${imageSeq}")
            .override(R.dimen.crew_detail_image_size,R.dimen.crew_detail_image_size)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
            .placeholder(R.drawable.img)
            .into(this)
        this.clipToOutline = true
    }
}