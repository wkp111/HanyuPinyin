package com.wkp.hanyupinyin.view.activity

import abc.abc.abc.nm.sp.SplashViewSettings
import abc.abc.abc.nm.sp.SpotListener
import abc.abc.abc.nm.sp.SpotManager
import abc.abc.abc.nm.sp.SpotRequestListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.utils.PermissionHelper
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Created by user on 2018/3/16.
 */
class ADActivity : AppCompatActivity() {

    private var mPermissionHelper: PermissionHelper? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        //预加载开屏广告
        SpotManager.getInstance(this).requestSpot(object : SpotRequestListener {
            override fun onRequestFailed(p0: Int) {
                initAD()
            }

            override fun onRequestSuccess() {
                initAD()
            }

        })
    }

    /**
     * 权限判断
     */
    private fun initAD() {
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = PermissionHelper(this)
        mPermissionHelper!!.setOnApplyPermissionListener { setupSplashAd() }
        if (Build.VERSION.SDK_INT < 23) {
            setupSplashAd()
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper!!.isAllRequestedPermissionGranted) {
                setupSplashAd()
            } else {
                mPermissionHelper!!.applyPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mPermissionHelper?.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 设置开屏广告
     */
    private fun setupSplashAd() {
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider)
        // 对开屏进行设置
        val splashViewSettings = SplashViewSettings()
        //		// 设置是否展示失败自动跳转，默认自动跳转
        //		splashViewSettings.setAutoJumpToTargetWhenShowFailed(false);
        // 设置跳转的窗口类
        splashViewSettings.targetClass = MainActivity::class.java
        // 设置开屏的容器
        splashViewSettings.splashViewContainer = rl_splash

        // 展示开屏广告
        SpotManager.getInstance(this)
                .showSplash(this, splashViewSettings, object : SpotListener {

                    override fun onShowSuccess() {
                    }

                    override fun onShowFailed(errorCode: Int) {
                    }

                    override fun onSpotClosed() {
                    }

                    override fun onSpotClicked(isWebPage: Boolean) {
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(this).onDestroy();
    }
}