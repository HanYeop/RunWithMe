package com.ssafy.runwithme.service

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.*
import com.ssafy.runwithme.utils.JWT
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataByPhoneListenerService : WearableListenerService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val dataClient by lazy { Wearable.getDataClient(this) }


    override fun onChannelOpened(p0: Channel) {
        super.onChannelOpened(p0)
        Log.d("test5", "onChannelOpened: ")
    }

    override fun onChannelClosed(p0: ChannelClient.Channel, p1: Int, p2: Int) {
        super.onChannelClosed(p0, p1, p2)
        Log.d("test5", "onChannelClosed: ")
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
        super.onCapabilityChanged(p0)
        Log.d("test5", "onCapabilityChanged: ")
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        dataEvents.forEach { event ->

            Log.d("test5", "onDataChanged: phone")

            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItemPath = event.dataItem.uri.path ?: ""
                if (dataItemPath.startsWith("/post")) {

//                    private val dataClient by lazy { Wearable.getDataClient(this) }
                    val isPost = DataMapItem.fromDataItem(event.dataItem).dataMap.getString("post")
                    if(isPost != "") {
                        CoroutineScope(Dispatchers.IO).launch {
                            postToken()
                        }
                    }
                }
            }
        }

    }

    private suspend fun postToken(){
        CoroutineScope(Dispatchers.IO).launch {
            val refresh = "refresh"
            val refreshReq = PutDataMapRequest.create("/auth").apply {
                dataMap.putString(JWT, refresh)
            }.asPutDataRequest().setUrgent()
            val task = dataClient.putDataItem(refreshReq)
            task.addOnCompleteListener {
                Log.d(com.ssafy.runwithme.utils.TAG, "postToken: refresh : ${it.isSuccessful}")
            }
        }.join()

        delay(1000L)

        CoroutineScope(Dispatchers.IO).launch {
            val token = sharedPreferences.getString(JWT, "")
            Log.d(com.ssafy.runwithme.utils.TAG, "postToken: token : $token ")
            val putDataReq = PutDataMapRequest.create("/auth").apply {
                dataMap.putString(JWT, token)
            }.asPutDataRequest().setUrgent()
            val putDataTask: Task<DataItem> = dataClient.putDataItem(putDataReq)
            putDataTask.addOnCompleteListener {
                Log.d("test5", "postToken: ${it.isSuccessful}")
            }
            Log.d(com.ssafy.runwithme.utils.TAG, "postToken: apikey : ${dataClient.apiKey}")
        }.join()

    }
}