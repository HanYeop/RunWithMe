package com.ssafy.runwithme.service

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.wearable.*
import com.ssafy.runwithme.utils.JWT
import com.ssafy.runwithme.utils.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    companion object{
        private val _changeMsgEvent = SingleLiveEvent<String>()
        val changeMsgEvent get() = _changeMsgEvent
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("test5", "onDataChanged: in first")
        super.onDataChanged(dataEvents)
        val dataClient = Wearable.getDataClient(this)
        Log.d("test5", "onDataChanged: dataClient in service : $dataClient")
        dataEvents.forEach { event ->
            Log.d("test5", "onDataChanged: in service")
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItemPath = event.dataItem.uri.path ?: ""
                if (dataItemPath.startsWith("/auth")) {
                    val token = DataMapItem.fromDataItem(event.dataItem).dataMap.getString(JWT)
//                    showToast(token)
                    Log.d("test5", "getData: token : $token")
                    if(token != "refresh") {
                        sharedPreferences.edit().putString(JWT, token).apply()
                        _changeMsgEvent.postValue("로그인 변경")
                        // Display interstitial screen to notify the user they are being signed in.
                        // Then, store the token and use it in network requests.
                    }
                }
            }
        }

    }
}