package com.wkp.hanyupinyin.model.impl

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Pair
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.iflytek.cloud.SpeechSynthesizer
import com.iflytek.cloud.SynthesizerListener
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_ENGLISH
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_FRENCH
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_JAPANESE
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_KOREAN
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_PORTUGUESE
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_RUSSIAN
import com.wkp.hanyupinyin.model.constants.LangTypeConstant.Companion.TYPE_SPANISH
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_DONG_BEI
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_HE_NAN
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_HU_NAN
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_PU_TONG
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_SHAN_XI
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_SI_CHUAN
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_TAI_WAN
import com.wkp.hanyupinyin.model.constants.VoiceConstant.Companion.VOICE_YUE_YU
import com.wkp.hanyupinyin.model.inter.IDetailAModel
import com.wkp.hanyupinyin.model.utils.PinyinUtil
import com.wkp.hanyupinyin.model.utils.YouDaoUtil
import com.wkp.hanyupinyin.presenter.callback.CallBack
import com.wkp.hanyupinyin.presenter.callback.SpeakCallBack
import com.youdao.sdk.app.LanguageUtils
import com.youdao.sdk.ydonlinetranslate.Translator
import com.youdao.sdk.ydtranslate.Translate
import com.youdao.sdk.ydtranslate.TranslateErrorCode
import com.youdao.sdk.ydtranslate.TranslateListener
import com.youdao.sdk.ydtranslate.TranslateParameters
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by user on 2018/3/14.
 * 详情展示界面Model
 */
class DetailAModelImpl : IDetailAModel {

    /**
     * 汉语转拼音
     */
    override fun getPinyin(hanYu: String, callBack: CallBack<MutableList<Pair<String, String>>>) {
        Observable.create<MutableList<Pair<String, String>>> {
            try {
                val pinyinString = PinyinUtil.convertToPinyinString(hanYu)
                it.onNext(pinyinString)
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callBack.onSuccess(it)
                }, {
                    callBack.onFailed(it)
                })
    }

    /**
     * 中文翻译为其他语言
     */
    override fun cnToOther(chinese: String, other: String, callBack: CallBack<String>) {
        //查词对象初始化，请设置source参数为app对应的名称（英文字符串）
        val langFrom = LanguageUtils.getLangByName("中文")
        //若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        //Language langFrom = LanguageUtils.getLangByName("自动");
        val langTo = LanguageUtils.getLangByName(other)
        val temp = "" + System.currentTimeMillis()
        val tps = TranslateParameters.Builder()
                .source(temp)
                .from(langFrom).to(langTo).build()
        val translator = Translator.getInstance(tps)
        translator.lookup(chinese, temp, object : TranslateListener {
            override fun onResult(result: Translate?, input: String?, requestId: String?) {
                Observable.create<String> {
                    val translations = result?.translations
                    if (translations != null) {
                        val sb = StringBuilder()
                        for (str in translations) {
                            sb.append(str)
                        }
                        it.onNext(sb.toString())
                    }
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callBack.onSuccess(it)
                        }
            }

            override fun onResult(results: MutableList<Translate>?, inputs: MutableList<String>?, errors: MutableList<TranslateErrorCode>?, requestId: String?) {

            }

            override fun onError(error: TranslateErrorCode?, requestId: String?) {
                Observable.create<Throwable> {
                    it.onNext(Throwable(error?.name))
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callBack.onFailed(it)
                        }
            }
        })
    }

    /**
     * 讯飞语音合成
     */
    override fun speaking(context: Context, chinese: String, voiceName: Int, callBack: SpeakCallBack<Int>) {
        var voice_name: String? = null
        when (voiceName) {
            VOICE_PU_TONG -> voice_name = "xiaoyan"
            VOICE_YUE_YU -> voice_name = "vixm"
            VOICE_TAI_WAN -> voice_name = "vixl"
            VOICE_SI_CHUAN -> voice_name = "vixr"
            VOICE_DONG_BEI -> voice_name = "vixyun"
            VOICE_HE_NAN -> voice_name = "vixk"
            VOICE_HU_NAN -> voice_name = "vixqa"
            VOICE_SHAN_XI -> voice_name = "vixying"
        }
        val synthesizer = SpeechSynthesizer.createSynthesizer(context, null)
        synthesizer.setParameter(SpeechConstant.VOICE_NAME, voice_name)
        synthesizer.setParameter(SpeechConstant.VOLUME, "75")
        synthesizer.setParameter(SpeechConstant.SPEED, "40")
        synthesizer.startSpeaking(chinese, object : SynthesizerListener {
            //合成进度
            override fun onBufferProgress(percent: Int, beginPos: Int, endPos: Int, info: String?) {
            }

            override fun onSpeakBegin() {
                Observable.create<Int> {
                    it.onNext(0)
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callBack.onSpeakBegin()
                        }
            }

            //播放进度
            override fun onSpeakProgress(percent: Int, beginPos: Int, endPos: Int) {
                Observable.create<Int> {
                    it.onNext(percent)
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            callBack.onSpeakProgress(it)
                        }
            }

            override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle?) {
            }

            override fun onSpeakPaused() {
            }

            override fun onSpeakResumed() {
            }

            //播放完成
            override fun onCompleted(error: SpeechError?) {
                Observable.create<Int> {
                    if (error == null) {
                        it.onNext(100)
                    } else {
                        it.onError(error)
                    }
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            callBack.onSuccess(it)
                        }, {
                            callBack.onFailed(it)
                        })
            }
        })
    }

    /**
     * 有道语音合成
     */
    override fun youDaoSpeak(string: String, langType: Int, context: Context, callBack: CallBack<Boolean>) {
        Observable.create<MutableList<String>> {
            val strList = ArrayList<String>()
            var sb: StringBuilder? = null
            for (i in 0 until string.length) {
                if (i % 400 == 0) {
                    if (sb != null) {
                        strList.add(getResultUrl(sb.toString(), langType))
                    }
                    sb = StringBuilder()
                    sb.append(string[i])
                } else {
                    sb?.append(string[i])
                }
            }
            strList.add(getResultUrl(sb.toString(), langType))
            it.onNext(strList)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val urlList = it
                    Observable.create<Boolean> {
                        var obser = it
                        var player: MediaPlayer? = null
                        if (urlList.size > 0){
                            player = MediaPlayer.create(context,Uri.parse(urlList[0]))
                            var next: MediaPlayer = player
                            for (i in 1 until urlList.size){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    val mediaPlayer = MediaPlayer.create(context, Uri.parse(urlList[i]))
                                    next.setNextMediaPlayer(mediaPlayer)
                                    next = mediaPlayer
                                }
                            }
                            player?.setOnCompletionListener {
                                it.release()
                                obser.onNext(true)
                            }
                            player?.setOnErrorListener { mp, what, extra ->
                                mp.release()
                                obser.onNext(false)
                                true
                            }
                            player?.start()
                        }else{
                            it.onNext(false)
                        }
                    }
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                callBack.onSuccess(it)
                            }
                }
    }

    /**
     * 获取链接
     */
    private fun getResultUrl(string: String, langType: Int): String {
        val map = HashMap<String, String>()
        val url = "http://openapi.youdao.com/ttsapi"
        when (langType) {
            TYPE_ENGLISH -> map["langType"] = "en"
            TYPE_JAPANESE -> map["langType"] = "ja"
            TYPE_KOREAN -> map["langType"] = "ko"
            TYPE_FRENCH -> map["langType"] = "fr"
            TYPE_RUSSIAN -> map["langType"] = "ru"
            TYPE_PORTUGUESE -> map["langType"] = "pt"
            TYPE_SPANISH -> map["langType"] = "es"
        }
        map["voice"] = "0"
        map["version"] = "v1"
        map["salt"] = "12345"
        map["appKey"] = "1c29b7d523e04143"
        map["q"] = string
        map["format"] = "wav"
        val sign = YouDaoUtil.md5(map["appKey"] + string + map["salt"] + "DQtQjsNOMYmBPPSATAukNvsG7Yh5MsjB")
        map["sign"] = sign
        return YouDaoUtil.getUrlWithQueryString(url, map)
    }
}