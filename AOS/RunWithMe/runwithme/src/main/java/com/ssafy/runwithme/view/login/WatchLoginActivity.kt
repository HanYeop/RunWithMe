package com.ssafy.runwithme.view.login


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityLoginBinding
import com.ssafy.runwithme.view.running.list.WatchMainActivity
import com.ssafy.runwithme.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.ssafy.runwithme.R
import com.ssafy.runwithme.service.DataLayerListenerService
import kotlinx.coroutines.*

@AndroidEntryPoint
class WatchLoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login), CapabilityClient.OnCapabilityChangedListener
{
    @Inject
    lateinit var sharedPref: SharedPreferences
    val userViewModel by viewModels<UserViewModel>()

    private lateinit var capabilityClient: CapabilityClient
    private lateinit var nodeClient: NodeClient
    private lateinit var remoteActivityHelper: RemoteActivityHelper

    private var androidPhoneNodeWithApp: Node? = null

    private val CAPABILITY_PHONE_APP = "verify_remote_example_phone_app"
    // 마켓 uri로 바꾸기
    private val ANDROID_MARKET_APP_URI =
        "market://details?id=com.example.android.wearable.wear.wearverifyremoteapp"

    private val dataClient by lazy { Wearable.getDataClient(this) }


    override fun init() {

        binding.apply {
//            layoutSwipe.addCallback(swipeCallback)
        }

        requestPermission{
            if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
                permissionDialog()
            }
        }

        capabilityClient = Wearable.getCapabilityClient(this)
        nodeClient = Wearable.getNodeClient(this)
        remoteActivityHelper = RemoteActivityHelper(this)

        autoLogin()

        initClickListener()

        initViewModelCallback()

        initLoginCallback()

    }


    private fun initClickListener() {
        binding.apply {
            btnLogin.setOnClickListener {
                showToast("폰에서 로그인해주세요")
                lifecycleScope.launch {
                    postMessageToPhone()
                }
            }
        }
    }

    private fun requestPermission(logic : () -> Unit){
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    logic()
                }
                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    showToast("권한을 허가해주세요.")
                }
            })
            .setDeniedMessage("앱 사용을 위해 권한을 허용으로 설정해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            .check()
    }

    private fun backgroundPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2)
    }

    private fun permissionDialog(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> backgroundPermission()
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)

        builder.show()
    }

//    private fun playPhoneApp(){
//        Log.d(TAG, "playPhoneApp: ")
//        val packageName = "com.ssafy.runwithme"
////        val intent = applicationContext.packageManager.getLaunchIntentForPackage(packageName)!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////        val a = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")).apply {
////            flags = Intent.FLAG_ACTIVITY_NEW_TASK
////            setPackage("com.ssafy.runwithme")
////        }
////        lifecycleScope.launch {
////            remoteActivityHelper.startRemoteActivity(a).await()
////            ConfirmationOverlay().showOn(this@WatchLoginActivity)
////        }
//
//        val intent = applicationContext.packageManager.getLaunchIntentForPackage(packageName)
//
////        startActivity(intent)
//    }

    private suspend fun postMessageToPhone(){
//        val dataClient = Wearable.getDataClient(this)
        Log.d(TAG, "postMessageToPhone: ")
        lifecycleScope.launch {
            val refreshDataReq = PutDataMapRequest.create("/post").apply {
                dataMap.putString("post", "")
            }.asPutDataRequest().setUrgent()
            val task = dataClient.putDataItem(refreshDataReq)
            task.addOnCompleteListener {
                Log.d(TAG, "postMessageToPhone: task ${it.isSuccessful}")
            }
        }.join()

        delay(1000L)

        lifecycleScope.launch {
            val putDataReq = PutDataMapRequest.create("/post").apply {
                dataMap.putString("post", "요청")
            }.asPutDataRequest().setUrgent()
            val putDataTask: Task<DataItem> = dataClient.putDataItem(putDataReq)
            putDataTask.addOnCompleteListener {
                Log.d("test5", "postToken: ${it.isSuccessful}")
            }
        }.join()

    }


    private fun initLoginCallback(){
        DataLayerListenerService.changeMsgEvent.observe(this){
            Log.d(TAG, "initLoginCallback: $it, token : ${sharedPref.getString(JWT, "")}")
            autoLogin()
        }
    }

    private fun initViewModelCallback() {
        userViewModel.joinMsgEvent.observe(this) {
            showToast(it)
        }

        userViewModel.errorMsgEvent.observe(this) {
            showToast(it)
        }

        userViewModel.loginEvent.observe(this) {
            showToast(it)
            val intent = Intent(this@WatchLoginActivity, WatchMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Log.d(TAG, "onCapabilityChanged(): $capabilityInfo")
        // There should only ever be one phone in a node set (much less w/ the correct
        // capability), so I am just grabbing the first one (which should be the only one).
        androidPhoneNodeWithApp = capabilityInfo.nodes.firstOrNull()
//        updateUi()
    }

    private suspend fun checkIfPhoneHasApp() {
        Log.d(TAG, "checkIfPhoneHasApp()")

        try {
            val capabilityInfo = capabilityClient.getCapability(CAPABILITY_PHONE_APP, CapabilityClient.FILTER_ALL).result

//                .await()

            Log.d(TAG, "Capability request succeeded.")

            withContext(Dispatchers.Main) {
                // There should only ever be one phone in a node set (much less w/ the correct
                // capability), so I am just grabbing the first one (which should be the only one).
                androidPhoneNodeWithApp = capabilityInfo.nodes.firstOrNull()
//                updateUi()
            }
        } catch (cancellationException: CancellationException) {
            // Request was cancelled normally
        } catch (throwable: Throwable) {
            Log.d(TAG, "Capability request failed to return any results.")
        }
    }

//    private fun openAppInStoreOnPhone() {
//        Log.d(TAG, "openAppInStoreOnPhone()")
//
//        val intent = when (PhoneTypeHelper.getPhoneDeviceType(applicationContext)) {
//            PhoneTypeHelper.DEVICE_TYPE_ANDROID -> {
//                Log.d(TAG, "\tDEVICE_TYPE_ANDROID")
//                // Create Remote Intent to open Play Store listing of app on remote device.
//                Intent(Intent.ACTION_VIEW)
//                    .addCategory(Intent.CATEGORY_BROWSABLE)
//                    .setData(Uri.parse(ANDROID_MARKET_APP_URI))
//            }
//            PhoneTypeHelper.DEVICE_TYPE_IOS -> {
//                Log.d(TAG, "\tDEVICE_TYPE_IOS")
//
//                // Create Remote Intent to open App Store listing of app on iPhone.
//                Intent(Intent.ACTION_VIEW)
//                    .addCategory(Intent.CATEGORY_BROWSABLE)
//                    .setData(Uri.parse(APP_STORE_APP_URI))
//            }
//            else -> {
//                Log.d(TAG, "\tDEVICE_TYPE_ERROR_UNKNOWN")
//                return
//            }
//        }
//
//        lifecycleScope.launch {
//            try {
//                remoteActivityHelper.startRemoteActivity(intent).await()
//
//                ConfirmationOverlay().showOn(this@LoginActivity)
//            } catch (cancellationException: CancellationException) {
//                // Request was cancelled normally
//                throw cancellationException
//            } catch (throwable: Throwable) {
//                ConfirmationOverlay()
//                    .setType(ConfirmationOverlay.FAILURE_ANIMATION)
//                    .showOn(this@LoginActivity)
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
//        Wearable.getDataClient(this).addListener(this)
        Wearable.getCapabilityClient(this).addListener(this, CAPABILITY_PHONE_APP)
        lifecycleScope.launch{
            checkIfPhoneHasApp()
        }
    }

    override fun onPause() {
        super.onPause()
//        Wearable.getDataClient(this).removeListener(this)
        Wearable.getCapabilityClient(this).removeListener(this, CAPABILITY_PHONE_APP)

    }

    private fun autoLogin() {
        val jwt = sharedPref.getString(JWT, "")

        if (jwt != "") {
            startActivity(Intent(this, WatchMainActivity::class.java))
            finish()
        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.EMAIL))
            .requestServerAuthCode(resources.getString(R.string.google_client_key))
            .requestEmail()
            .requestIdToken(getString(R.string.google_client_key))
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        googleSignInResult.launch(signInIntent)
    }

    private val googleSignInResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val accessToken = account.idToken!!

                userViewModel.googleLogin(accessToken)
            } catch (e: ApiException) {
                Log.w("test5", "signInResult:failed code=" + e.statusCode)
            }
        } else {
            Log.d("test5", "$it: ")
        }
    }

    val TAG = "test5"
    private fun kakaoSignIn() {
        Log.e(TAG, "카카오1")
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            Log.e(TAG, "카카오8")
            if (error != null) {
                Log.e(TAG, "카카오2")
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.e(TAG, "카카오3")
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                userViewModel.kakaoLogin(token.accessToken)

                // 사용자 정보 요청 (기본)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오4")
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.e(TAG, "카카오5")
                        Log.i(
                            TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                        )
                    } else {
                        Log.e(TAG, "카카오6")
                    }
                }
            } else {
                Log.e(TAG, "카카오7")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        } else if (user != null) {
                            Log.i(
                                TAG, "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}" +
                                        "\n이메일: ${user.kakaoAccount?.email}" +
                                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                            )
                        }
                    }
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    userViewModel.kakaoLogin(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }


    private fun naverSignIn() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        val email = result.profile?.email.toString()
                        Log.d(TAG, "onSuccess: ${result.profile}")
                        Log.d(TAG, "onSuccess: $email")

                        Log.d(TAG, "onSuccess: ${NaverIdLoginSDK.getAccessToken()!!}")
                        userViewModel.naverLogin(NaverIdLoginSDK.getAccessToken()!!, email)
                    }

                    override fun onError(errorCode: Int, message: String) {}
                    override fun onFailure(httpStatus: Int, message: String) {}
                })
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                showToast("errorCode:$errorCode, errorDesc:$errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }
}