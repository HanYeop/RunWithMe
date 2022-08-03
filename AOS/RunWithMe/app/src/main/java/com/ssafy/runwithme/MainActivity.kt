package com.ssafy.runwithme

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.runwithme.base.BaseActivity
import com.ssafy.runwithme.databinding.ActivityMainBinding
import com.ssafy.runwithme.service.RunningService
import com.ssafy.runwithme.utils.ACTION_SHOW_TRACKING_ACTIVITY
import com.ssafy.runwithme.view.running.RunningActivity
import com.ssafy.runwithme.view.running.list.RunningListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController : NavController

    override fun init() {
        requestPermission{
            if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
                permissionDialog()
            }
        }
        initNavigation()

        runningCheck()

        initClickListener()
    }

    private fun initClickListener(){
        binding.apply {
            floatingActionButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, RunningListActivity::class.java))
            }
        }
    }


    private fun runningCheck(){
        // 트래킹이 종료되지 않았을 때, 백그라운드에서 제거 후 실행해도 바로 트래킹 화면이 뜨게함
        if(RunningService.isFirstRun){
            val intent = Intent(this, MainActivity::class.java).also {
                it.action = ACTION_SHOW_TRACKING_ACTIVITY
            }
            navigateToTrackingFragmentIfNeeded(intent)
        }
    }

    // foreground 상태에서 호출
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    // 알림 창 클릭시 메인 -> Tracking
    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_ACTIVITY) {
            startActivity(Intent(this, RunningActivity::class.java))
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

    private fun backgroundPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2)
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.expandableBottomBar.apply {
            setupWithNavController(navController)
            background = null
        }

        // TODO : 애니메이션 TEST
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 바텀 네비게이션이 표시되는 Fragment
            if(destination.id == R.id.HomeFragment || destination.id == R.id.MyPageFragment){
                if(binding.expandableBottomBar.visibility == View.GONE) {
                    val animation = AlphaAnimation(0f, 1f)
                    animation.duration = 500
                    binding.expandableBottomBar.visibility = View.VISIBLE
                    binding.expandableBottomBar.animation = animation
                }
            }
            // 바텀 네비게이션이 표시되지 않는 Fragment
            else{
                if(binding.expandableBottomBar.visibility == View.VISIBLE) {
                    val animation = AlphaAnimation(1f, 0f)
                    animation.duration = 500
                    binding.expandableBottomBar.visibility = View.GONE
                    binding.expandableBottomBar.animation = animation
                }
            }
        }
    }

    // 홈 화면에서 뒤로가기 2번 클릭 시 종료
    var waitTime = 0L
    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.HomeFragment) {
            if (System.currentTimeMillis() - waitTime >= 1500) {
                waitTime = System.currentTimeMillis()
                Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
        else{
            super.onBackPressed()
        }
    }
}