package com.wkp.hanyupinyin.view.application

import abc.abc.abc.AdManager
import android.app.Application
import android.os.Build
import android.support.annotation.RequiresApi
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.lzy.imagepicker.ImagePicker
import com.wkp.hanyupinyin.model.external.GlideImageLoader
import com.youdao.sdk.app.YouDaoApplication


/**
 * Created by user on 2018/3/9.
 * 程序application
 */
@RequiresApi(Build.VERSION_CODES.M)
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initImagePicker()
        initBaiDuOCR()
        initXunFei()
        initYouDao()
        initYouMi()
    }

    /**
     * 初始化有米广告
     */
    private fun initYouMi() {
        AdManager.getInstance(this).init("f53cb46b1b54d4d7", "c4cc22cd3a166fcc", false)
    }

    /**
     * 有道翻译
     */
    private fun initYouDao() {
        YouDaoApplication.init(this, "1c29b7d523e04143")
    }

    /**
     * 讯飞语音
     */
    private fun initXunFei() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5aa643d3")
    }

    /**
     * 百度OCR
     */
    private fun initBaiDuOCR() {
        OCR.getInstance().initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(result: AccessToken) {
                // 调用成功，返回AccessToken对象
                val token = result.accessToken
            }

            override fun onError(error: OCRError) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, this)
    }

    /**
     * 初始化图库加载工具
     */
    private fun initImagePicker() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.isMultiMode = false
        imagePicker.imageLoader = GlideImageLoader()   //设置图片加载器
        imagePicker.isShowCamera = false  //显示拍照按钮
        imagePicker.isCrop = false        //允许裁剪（单选才有效）
//        imagePicker.isSaveRectangle = true //是否按矩形区域保存
//        imagePicker.selectLimit = 9    //选中数量限制
//        imagePicker.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
//        imagePicker.focusWidth = 800   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.focusHeight = 800  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.outPutX = 1000//保存文件的宽度。单位像素
//        imagePicker.outPutY = 1000//保存文件的高度。单位像素
    }
}