package com.wkp.hanyupinyin.view.inter

/**
 * Created by user on 2018/3/7.
 */
interface IMainAView {
    companion object {
        //请求
        const val REQUEST_FLAG_CONTEXT = 1            //上下文
        //响应
        const val RESPONSE_FLAG_NO_PERMISSION = -1    //无外存储权限
        const val RESPONSE_FLAG_NO_BITMAP = -2        //路径无图片
        const val RESPONSE_FLAG_NO_NETWORK = -3       //无网络
        const val RESPONSE_FLAG_NO_TEXT = -4          //扫描图片无文本
        const val RESPONSE_FLAG_SCAN_TEXT = -5        //扫描图片文本
        const val RESPONSE_FLAG_SPEECH_ERROR = -6     //语音录入异常
        const val RESPONSE_FLAG_SPEECH_TEXT = -7      //语音录入文本
    }

    fun <T> request(requestFlag: Int): T?

    fun <T> response(responseFlag: Int, response: T?)
}