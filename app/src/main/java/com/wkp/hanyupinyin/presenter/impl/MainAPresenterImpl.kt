package com.wkp.hanyupinyin.presenter.impl

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import com.wkp.hanyupinyin.model.impl.MainAModelImpl
import com.wkp.hanyupinyin.model.inter.IMainAModel
import com.wkp.hanyupinyin.model.utils.NetWorkUtils
import com.wkp.hanyupinyin.presenter.callback.CallBack
import com.wkp.hanyupinyin.presenter.inter.IMainAPresenter
import com.wkp.hanyupinyin.view.inter.IMainAView
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.REQUEST_FLAG_CONTEXT
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_NO_PERMISSION
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_SPEECH_ERROR
import com.wkp.runtimepermissions.util.RuntimePermissionUtil

/**
 * Created by user on 2018/3/9.
 * 主界面presenter
 */
@RequiresApi(Build.VERSION_CODES.M)
class MainAPresenterImpl(iMainAView: IMainAView): IMainAPresenter {

    var iMainAModel: IMainAModel? = null
    var iMainAView: IMainAView? = null

    init {
        iMainAModel = MainAModelImpl()
        this.iMainAView = iMainAView
    }

    /**
     * 百度文本识别
     */
    override fun scanText(path: String) {
        RuntimePermissionUtil.checkPermissions(iMainAView?.request<Context>(REQUEST_FLAG_CONTEXT), RuntimePermissionUtil.STORAGE, {
            if (!it) {
                iMainAView?.response<Throwable>(RESPONSE_FLAG_NO_PERMISSION,Throwable("No storage permission!"))
                return@checkPermissions
            }
            val textBitmap = BitmapFactory.decodeFile(path)
            if (textBitmap == null) {
                iMainAView?.response<Throwable>(IMainAView.RESPONSE_FLAG_NO_BITMAP,Throwable("Path no bitmap!"))
                return@checkPermissions
            }
            if (!NetWorkUtils.isNetworkConnected(iMainAView?.request<Context>(REQUEST_FLAG_CONTEXT))) {
                iMainAView?.response<Throwable>(IMainAView.RESPONSE_FLAG_NO_NETWORK,Throwable("No network!"))
                return@checkPermissions
            }
            iMainAModel?.scanTextBaiDu(path,true, object : CallBack<String>{
                override fun onSuccess(success: String) {
                    iMainAView?.response<String>(IMainAView.RESPONSE_FLAG_SCAN_TEXT,success)
                }

                override fun onFailed(throwable: Throwable) {
                    iMainAModel?.scanTextBaiDu(path,false, object : CallBack<String>{
                        override fun onSuccess(success: String) {
                            iMainAView?.response<String>(IMainAView.RESPONSE_FLAG_SCAN_TEXT,success)
                        }

                        override fun onFailed(throwable: Throwable) {
                            iMainAView?.response<Throwable>(IMainAView.RESPONSE_FLAG_NO_TEXT,throwable)
                        }
                    })
                }
            })
        })
    }

    /**
     * 讯飞语音听写
     */
    override fun speech() {
        RuntimePermissionUtil.checkPermissions(iMainAView?.request<Context>(REQUEST_FLAG_CONTEXT),RuntimePermissionUtil.MICROPHONE,{
            if (it) {
                iMainAModel?.speech(iMainAView?.request<Context>(REQUEST_FLAG_CONTEXT), object : CallBack<String>{
                    override fun onSuccess(success: String) {
                        iMainAView?.response<String>(IMainAView.RESPONSE_FLAG_SPEECH_TEXT,success)
                    }

                    override fun onFailed(throwable: Throwable) {
                        iMainAView?.response<Throwable>(RESPONSE_FLAG_SPEECH_ERROR,Throwable("Speech error!"))
                    }

                })
            }else{
                iMainAView?.response<Throwable>(RESPONSE_FLAG_NO_PERMISSION,Throwable("No record audio permission!"))
            }
        })
    }
}