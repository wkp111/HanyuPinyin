package com.wkp.hanyupinyin.model.impl

import android.content.Context
import android.text.TextUtils
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.GeneralBasicParams
import com.baidu.ocr.sdk.model.GeneralParams
import com.baidu.ocr.sdk.model.GeneralResult
import com.iflytek.cloud.RecognizerResult
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.wkp.hanyupinyin.model.constants.SPConstant
import com.wkp.hanyupinyin.model.inter.IMainAModel
import com.wkp.hanyupinyin.model.utils.IFlyUtil
import com.wkp.hanyupinyin.model.utils.SPUtils
import com.wkp.hanyupinyin.presenter.callback.CallBack
import java.io.File

/**
 * Created by user on 2018/3/9.
 * 主界面Model
 */
class MainAModelImpl: IMainAModel {
    /**
     * 讯飞支持语种：普通话、美式英语、粤语、四川话
     */
    private val LANGUAGES = arrayOf("mandarin","en_us","cantonese","lmz")

    /**
     * 百度文字识别
     */
    override fun scanTextBaiDu(filePath: String, containsLocation: Boolean, callBack: CallBack<String>) {
        // 通用文字识别参数设置(是否包含位置信息)
        val param = if (containsLocation) GeneralParams() else GeneralBasicParams()
        param.setDetectDirection(true)
        param.imageFile = File(filePath)

        // 调用通用文字识别服务
        OCR.getInstance().recognizeGeneralBasic(param, object : OnResultListener<GeneralResult> {
            override fun onResult(result: GeneralResult) {
                val sb = StringBuilder()
                // 调用成功，返回GeneralResult对象
                for (wordSimple in result.wordList) {
                    // wordSimple不包含位置信息
                    sb.append(wordSimple.words)
                }
                callBack.onSuccess(sb.toString())
            }

            override fun onError(error: OCRError) {
                // 调用失败，返回OCRError对象
                callBack.onFailed(error)
            }
        })
    }

    /**
     * 讯飞语音听写
     */
    override fun speech(context: Context?,callBack: CallBack<String>) {
        val language = LANGUAGES[SPUtils.getInt(context, SPConstant.SP_KEY_VOICE_LANGUAGE)]
        val recognizer = RecognizerDialog(context,null)
        if (TextUtils.equals(LANGUAGES[1],language)) {
            recognizer.setParameter(SpeechConstant.LANGUAGE,language)   //设置语言 英语
            recognizer.setParameter(SpeechConstant.ASR_SCH,"1")         //翻译开启
            recognizer.setParameter(SpeechConstant.ADD_CAP,"translate") //翻译模式
            recognizer.setParameter(SpeechConstant.ORI_LANG,"en")       //原文英语
            recognizer.setParameter(SpeechConstant.TRANS_LANG,"cn")     //译文中文
            recognizer.setParameter(SpeechConstant.TRS_SRC,"its")       //翻译结果格式
        }else{
            recognizer.setParameter(SpeechConstant.LANGUAGE,"zh_cn")    //设置语言 中文
            recognizer.setParameter(SpeechConstant.ACCENT,language)     //设置方言
        }
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_MIX) //设置引擎模式 云-本地混合
        recognizer.setParameter(SpeechConstant.VAD_EOS,"2000")          //设置后端点超时
        recognizer.setCanceledOnTouchOutside(false)
        recognizer.setCancelable(false)
        if (!recognizer.isShowing) {
            recognizer.setListener(object : RecognizerDialogListener{

                override fun onResult(p0: RecognizerResult?, p1: Boolean) {
                    callBack.onSuccess(IFlyUtil.parseIatResult(p0))
                }

                override fun onError(p0: SpeechError?) {
                    if (p0 != null) {
                        callBack.onFailed(p0)
                    }
                }

            })
            recognizer.show()
        }
    }

}