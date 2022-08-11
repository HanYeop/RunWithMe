package com.ssafy.runwithme.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ssafy.runwithme.R
import com.ssafy.runwithme.utils.BASE_URL
import java.lang.Math.round
import java.util.*

object ViewBindingAdapter {

    @BindingAdapter("startTime", "endTime")
    @JvmStatic
    fun TextView.setStartTime(timeStart: String, timeEnd: String){
        val startToken = StringTokenizer(timeStart, ":")
        val startHour = startToken.nextToken()
        val startMinute = startToken.nextToken()

        val endToken = StringTokenizer(timeEnd, ":")
        val endHour = endToken.nextToken()
        val endMinute = endToken.nextToken()

        this.text = "$startHour:$startMinute ~ $endHour:$endMinute"
    }

    @BindingAdapter("crewHorizonImage")
    @JvmStatic
    fun ImageView.setCrewHorizonImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.crew_image)
                .override(R.dimen.crew_horizon_image_size * 2,R.dimen.crew_horizon_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_horizon_image_size * 2,R.dimen.crew_horizon_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
    }


    @BindingAdapter("goalType", "goalAmount")
    @JvmStatic
    fun TextView.setGoal (goalType: String, goalAmount: Int){
        if(goalType == "distance"){
            this.text = "${goalAmount / 1000}km"
        }else{
            this.text = "${goalAmount / 60}분"
        }
    }

    @BindingAdapter("achievementContent")
    @JvmStatic
    fun TextView.setAchievementContent(name: String){
        this.text = "처음으로 $name 완주하기"
    }

    @BindingAdapter("totalDistanceConverter")
    @JvmStatic
    fun TextView.setTotalDistanceConverter (distance: Int){
        this.text = "${round(1.0 * distance / 1000.0 * 10.0) / 10.0} km"
    }

    @BindingAdapter("achievementImageType", "achievementImageValue")
    @JvmStatic
    fun ImageView.setAchievementImage (type: String, value: Double){
        val img = type.lowercase() + value.toInt().toString()
        val packageName = "com.ssafy.runwithme"
        val resId = this.resources.getIdentifier(img, "drawable", packageName)

        Glide.with(this.context)
            .load(resId)
            .override(120 * 2,120 * 2)
            .placeholder(R.drawable.img)
            .into(this)
    }

}