package com.wkp.hanyupinyin.model.utils

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.iflytek.cloud.RecognizerResult
import com.wkp.hanyupinyin.model.bean.TransResult
import com.wkp.hanyupinyin.model.bean.VoiceResult

/**
 * Created by user on 2018/3/14.
 * 讯飞工具类
 */
class IFlyUtil {
    companion object {
        /**
         * 解析语音听写结果
         */
        fun parseIatResult(result: RecognizerResult?): String {
            try {
                val gson = Gson()
                val resultString = result?.resultString
                if (!resultString?.contains("sid")!!){
                    val voiceResult = gson.fromJson<VoiceResult>(resultString, VoiceResult::class.java)
                    if (voiceResult != null) {
                        val ws = voiceResult.ws
                        if (ws != null) {
                            val sb = StringBuilder()
                            for (w in ws) {
                                val cw = w.cw
                                if (cw != null) {
                                    for (cwBean in cw) {
                                        sb.append(cwBean.w)
                                    }
                                }
                            }
                            return sb.toString()
                        }
                    }
                } else{
                    val transResult = gson.fromJson<TransResult>(result.resultString, TransResult::class.java)
                    if (transResult != null) {
                        val resultBean = transResult.trans_result
                        if (resultBean != null) {
                            return resultBean.dst
                        }
                    }
                }
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}