package com.ssafy.runwithme.binding

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ssafy.runwithme.R
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
                .load(R.drawable.crew_image)
                .override(R.dimen.crew_horizon_image_size * 2,R.dimen.crew_horizon_image_size * 2)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_horizon_image_size * 2,R.dimen.crew_horizon_image_size * 2)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }
    }

    @BindingAdapter("crewDetailImage")
    @JvmStatic
    fun ImageView.setCrewDetailImage (imageSeq: Int){
        Log.d("test5", "setCrewDetailImage: $imageSeq")
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.crew_image)
                .override(R.dimen.crew_detail_image_size * 2,R.dimen.crew_detail_image_size * 2)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_detail_image_size * 2, R.dimen.crew_detail_image_size * 2)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }

    }

    @BindingAdapter("crewBoardProfileImage")
    @JvmStatic
    fun ImageView.setCrewBoardProfileImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.user_image)
                .override(R.dimen.crew_board_profile_image_size * 2,R.dimen.crew_board_profile_image_size * 2)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_board_profile_image_size * 2,R.dimen.crew_board_profile_image_size * 2)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("myPageProfileImage")
    @JvmStatic
    fun ImageView.setMyPageProfileImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.user_image)
                .override(R.dimen.my_page_profile_image_size * 2,R.dimen.my_page_profile_image_size * 2)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true))
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.my_page_profile_image_size * 2,R.dimen.my_page_profile_image_size * 2)
                .apply(
                    RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).skipMemoryCache(true)
                )
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }
}