package com.ssafy.runwithme.binding

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ssafy.runwithme.R
import com.ssafy.runwithme.model.dto.CrewBoardDto
import com.ssafy.runwithme.model.dto.Weather
import com.ssafy.runwithme.utils.BASE_URL
import com.ssafy.runwithme.utils.Result
import com.ssafy.runwithme.utils.TAG
import java.lang.Math.round
import java.text.DecimalFormat
import java.text.NumberFormat
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

    @BindingAdapter("startDay", "endDay")
    @JvmStatic
    fun TextView.setDay(dayStart : String, dayEnd : String){
        if(dayStart != "" && dayEnd != "") {
            val startToken = StringTokenizer(dayStart, " ")
            val startDay = startToken.nextToken()

            val endToken = StringTokenizer(dayEnd, " ")
            val endDay = endToken.nextToken()

            this.text = "$startDay ~ $endDay"
        }
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

    @BindingAdapter("crewDetailImage")
    @JvmStatic
    fun ImageView.setCrewDetailImage (imageSeq: Int){
        Log.d("test5", "setCrewDetailImage: $imageSeq")
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.crew_image)
                .override(R.dimen.crew_detail_image_size * 2,R.dimen.crew_detail_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_detail_image_size * 2, R.dimen.crew_detail_image_size * 2)
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
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_board_profile_image_size * 2,R.dimen.crew_board_profile_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("rankingProfileImage")
    @JvmStatic
    fun ImageView.setRankingProfileImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.user_image)
                .override(R.dimen.ranking_profile_image_size * 2,R.dimen.ranking_profile_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.ranking_profile_image_size * 2,R.dimen.ranking_profile_image_size * 2)
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
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.my_page_profile_image_size * 2,R.dimen.my_page_profile_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("costFormat")
    @JvmStatic
    fun AppCompatButton.setCostFormat (cost: String){
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(cost.toInt())
        this.setText(formattedNumber)
    }

    @BindingAdapter("textCostFormat")
    @JvmStatic
    fun TextView.setTextCostFormat (cost: Int){
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(cost)
        this.setText(formattedNumber)
    }

    @BindingAdapter("isDistance", "distance", "time")
    @JvmStatic
    fun AppCompatButton.setPurposeType (isDistance: Boolean, distance: String, time: String){
        if(isDistance){
            this.setText(distance)
        }else{
            this.setText(time)
        }
    }

    @BindingAdapter("crewImage")
    @JvmStatic
    fun ImageView.setCrewImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.crew_image)
                .override(R.dimen.crew_image_size * 2,R.dimen.crew_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.crew_image_size * 2,R.dimen.crew_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
    }

    @BindingAdapter("imgCrewPasswd")
    @JvmStatic
    fun ImageView.setPasswdVisible (passwd: String?){
        if(passwd == null){
            this.visibility = View.INVISIBLE
        }else{
            this.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("goalDays")
    @JvmStatic
    fun TextView.setGoalDays (goalDays: Int){
        this.text = "주 $goalDays 회"
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

    @BindingAdapter("unit", "rankingValue")
    @JvmStatic
    fun TextView.synUnit(unit : String, rankingValue : Int){
        this.text = when(unit){
            "km" -> {
                val df = DecimalFormat("###0.000")
                df.format((rankingValue / 1000f))
            }
            "분" -> { (rankingValue / 60).toString() }
            "P" -> { "$rankingValue" }
            else -> ""
        }
    }

    @BindingAdapter("homeRankingValue")
    @JvmStatic
    fun TextView.synHomeUnit(rankingValue : Int){
        if(rankingValue > 0){
            val df = DecimalFormat("###0.000")
            this.text = df.format((rankingValue / 1000f)) + "km"
        } else {
            this.text = ""
        }
    }


    @BindingAdapter("runRecordImage")
    @JvmStatic
    fun ImageView.setRecordImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.running_record)
                .override(R.dimen.run_record_image_size * 2,R.dimen.run_record_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.run_record_image_size * 2,R.dimen.run_record_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
    }

    @BindingAdapter("distanceConverter")
    @JvmStatic
    fun TextView.setDistanceConverter (distance: Int){
        this.text = "${round(1.0 * distance / 1000.0 * 100.0) / 100.0}"
    }

    @BindingAdapter("timeConverter")
    @JvmStatic
    fun TextView.setTimeConverter (time: Int){
        this.text = "${time / 60}"
    }

    @BindingAdapter("calorieConverter")
    @JvmStatic
    fun TextView.setCalorieConverter (calorie: Int){
        this.text = "$calorie"
    }

    @BindingAdapter("totalCalorieConverter")
    @JvmStatic
    fun TextView.setTotalCalorieConverter (calorie: Double){
        var calorieInt = calorie.toInt()
        this.text = "$calorieInt kcal"
    }

    @BindingAdapter("speedConverter")
    @JvmStatic
    fun TextView.setSpeedConverter (speed: Double){
        this.text = "${round(speed * 10.0) / 10.0 }"
    }

    @BindingAdapter("totalSpeedConverter")
    @JvmStatic
    fun TextView.setTotalSpeedConverter (speed: Double){
        this.text = "${round(speed * 10.0) / 10.0 } km/h"
    }

    @BindingAdapter("myUserSeq", "board")
    @JvmStatic
    fun ImageView.setVisibility (userSeq: Int, board: CrewBoardDto){
        if(userSeq == board.userSeq){
            this.visibility = View.VISIBLE
        }else{
            this.visibility = View.GONE
        }
    }

    @BindingAdapter("hardPoint")
    @JvmStatic
    fun com.willy.ratingbar.ScaleRatingBar.setHardPoint(point: Int){
        this.rating = point.toFloat()
    }

    @BindingAdapter("homePageProfileImage")
    @JvmStatic
    fun ImageView.setHomePageProfileImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.user_image)
                .override(60 * 2,60 * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(60 * 2,60 * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        this.clipToOutline = true
    }

    @BindingAdapter("goalTypeConverter")
    @JvmStatic
    fun TextView.setGoalTypeConverter(goalType: String){
        if(goalType == "distance"){
            this.text = "거리"
        }else{
            this.text = "시간"
        }
    }

    @BindingAdapter("isCrewMember", "crewState")
    @JvmStatic
    fun AppCompatButton.setCrewState(isCrewMember: Boolean, crewState: String){
        if(isCrewMember){
            if(crewState == "await"){
                if(this.id == R.id.btn_join_crew){
                    this.visibility = View.GONE
                }else if(this.id == R.id.btn_resign_crew){
                    this.visibility = View.VISIBLE
                }else if(this.id == R.id.btn_running){
                    this.visibility = View.GONE
                }

            }else if(crewState == "start"){
                if(this.id == R.id.btn_join_crew){
                    this.visibility = View.GONE
                }else if(this.id == R.id.btn_resign_crew){
                    this.visibility = View.GONE
                }else if(this.id == R.id.btn_running){
                    this.visibility = View.VISIBLE
                }

            }else{
                this.visibility = View.GONE
            }

        }else{
            if(this.id == R.id.btn_join_crew){
                this.visibility = View.VISIBLE
            }else if(this.id == R.id.btn_resign_crew){
                this.visibility = View.GONE
            }else if(this.id == R.id.btn_running){
                this.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("isCrewMember", "crewState", "isCrewManager")
    @JvmStatic
    fun AppCompatButton.setCrewState(isCrewMember: Boolean, crewState: String, isCrewManager: Boolean){
        if(isCrewMember){
            if(crewState == "await"){
                this.visibility = View.VISIBLE

                if(isCrewManager){
                    this.text = "해체"
                }else{
                    this.text = "탈퇴"
                }

            }else if(crewState == "start"){
                if(this.id == R.id.btn_resign_crew){
                    this.visibility = View.GONE
                }

            }else{
                this.visibility = View.GONE
            }

        }else{
           if(this.id == R.id.btn_resign_crew){
                this.visibility = View.GONE
            }
        }
    }

    @BindingAdapter("createRecommendImage")
    @JvmStatic
    fun ImageView.setCreateRecommendImage (imageSeq: Int){
        if(imageSeq == 0){
            Glide.with(this.context)
                .load(R.drawable.running_record)
                .override(R.dimen.create_recommend_image_size * 2,R.dimen.create_recommend_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
        else {
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .override(R.dimen.create_recommend_image_size * 2,R.dimen.create_recommend_image_size * 2)
                .placeholder(R.drawable.img)
                .into(this)
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

    @BindingAdapter("totalTimeConverter")
    @JvmStatic
    fun TextView.setTotalTimeConverter (time: Int){
//        var minute = (time / 60).toString()
//
//        var hourInt = (time / 3600)
//        var text = "$minute 분"
//        if(hourInt != 0){
//            text = "$hourInt 시 " + text
//        }

        var minuteInt = (time % 3600)/ 60
        var minute = minuteInt.toString()
        if(minuteInt < 10){
            minute = "0" + minute
        }

        var hour = (time / 3600).toString()

        text = "${hour}시간 ${minute}분"
        this.text = text
    }

    @BindingAdapter("practiceImage")
    @JvmStatic
    fun ImageView.setCreateRecommendImage (img: Bitmap){
        Glide.with(this.context)
            .load(img)
            .override(60 * 2,60 * 2)
            .placeholder(R.drawable.img)
            .into(this)
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

    @BindingAdapter("myGoal", "totalGoal")
    @JvmStatic
    fun TextView.setProgress (myGoal: Int, totalGoal : Int){
        var progress : Int = 0

        if(myGoal != 0){
            progress = ((myGoal.toDouble() / totalGoal.toDouble()) * 100).toInt()
        }
        // 100 달성의 경우 색변경
        if(progress == 100){
            var color = resources.getColor(R.color.mainColor)
            this.setTextColor(color)
        }

        this.text = "$progress%"
    }

    @BindingAdapter("myGoal", "totalGoal")
    @JvmStatic
    fun ProgressBar.setProgress (myGoal: Int, totalGoal : Int){
        var progress : Int = 0

        if(myGoal != 0){
            progress = ((myGoal.toDouble() / totalGoal.toDouble()) * 100).toInt()
        }
        // 100 달성의 경우 색변경
        if(progress == 100){
            var color = resources.getColor(R.color.mainColor)
            this.progressTintList = ColorStateList.valueOf(color)
        }

        this.progress = progress
    }

    @BindingAdapter("weatherImage")
    @JvmStatic
    fun ImageView.setWeatherImage (fcstValue: Int){
        if(fcstValue == 0){
            Glide.with(this.context)
                .load(R.drawable.sun)
                .override(24 * 2,24 * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }else{
            Glide.with(this.context)
                .load(R.drawable.rain)
                .override(24 * 2,24 * 2)
                .placeholder(R.drawable.img)
                .into(this)
        }
    }

    @BindingAdapter("competitionResult")
    @JvmStatic
    fun ImageView.setCompetitonResult (result: String?){
        this.visibility = View.VISIBLE
        if(result == "FIRST"){
            Glide.with(this.context)
                .load(R.drawable.gold_cup)
                .into(this)
        }else if(result == "SECOND"){
            Glide.with(this.context)
                .load(R.drawable.silver_cup)
                .into(this)
        }else if(result == "THIRD"){
            Glide.with(this.context)
                .load(R.drawable.bronze_cup)
                .into(this)
        }else{
            this.visibility = View.GONE
        }
    }

    @BindingAdapter("competitionImage")
    @JvmStatic
    fun ImageView.setCompetitionImage(imageSeq: Int){
        if(imageSeq == 0){

        }else{
            Glide.with(this.context)
                .load("${BASE_URL}images/${imageSeq}")
                .into(this)
        }
    }

    @BindingAdapter("competitionJoinPossible")
    @JvmStatic
    fun AppCompatButton.setJoinPossible(participant : Boolean){
        if(participant){
            this.visibility = View.GONE
        }else{
            this.visibility = View.VISIBLE
        }

    }
}