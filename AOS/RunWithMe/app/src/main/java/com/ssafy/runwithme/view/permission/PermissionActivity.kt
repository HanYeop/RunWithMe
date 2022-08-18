package com.ssafy.runwithme.view.permission

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.runwithme.R
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityPermissionBinding
import com.ssafy.runwithme.utils.PERMISSION_OK
import com.ssafy.runwithme.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissionBinding>(R.layout.activity_permission) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun init() {
        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            btnOk.setOnClickListener {
                requestPermission{
                    if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_DENIED) {
                        start()
                    }
                }
            }
            tvTerms.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://hanyeop.github.io/RunWithMe-terms/")
                startActivity(i)
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
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun start(){
        sharedPreferences.edit().putBoolean(PERMISSION_OK,true).apply()
        startActivity(Intent(this@PermissionActivity,LoginActivity::class.java))
        finish()
    }
}