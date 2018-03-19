package com.wkp.hanyupinyin.view.inter

/**
 * Created by user on 2018/3/14.
 */
interface IDetailAView {
    companion object {
        //请求
        const val REQUEST_FLAG_CONTEXT = 1            //上下文
        //响应
        const val RESPONSE_FLAG_NO_TEXT = -1          //转拼音无文本
        const val RESPONSE_FLAG_CONVERT_ERROR = -2    //转拼音错误
        const val RESPONSE_FLAG_PINYIN_TEXT = -3      //转拼音成功文本
        const val RESPONSE_FLAG_TRANS_ERROR = -4      //翻译错误
        const val RESPONSE_FLAG_TRANS_RESULT = -5     //翻译成功文本
        const val RESPONSE_FLAG_SPEAK_BEGIN = -6      //语音合成发音开始
        const val RESPONSE_FLAG_SPEAK_PROGRESS = -7   //语音合成发音进度
        const val RESPONSE_FLAG_SPEAK_ERROR = -8      //语音合成发音错误
        const val RESPONSE_FLAG_SPEAK_COMPLETE = -9   //语音合成发音完成
        const val RESPONSE_FLAG_YOUDAO_SPEAK_RESULT = -10   //有道语音合成发音完成
    }

    fun <T> request(requestFlag: Int): T?

    fun <T> response(responseFlag: Int, response: T?)
}