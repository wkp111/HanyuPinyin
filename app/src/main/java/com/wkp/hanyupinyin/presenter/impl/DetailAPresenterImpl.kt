package com.wkp.hanyupinyin.presenter.impl

import android.content.Context
import android.text.TextUtils
import android.util.Pair
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.bean.SpeakProgress
import com.wkp.hanyupinyin.model.bean.TransOtherResult
import com.wkp.hanyupinyin.model.impl.DetailAModelImpl
import com.wkp.hanyupinyin.model.inter.IDetailAModel
import com.wkp.hanyupinyin.presenter.callback.CallBack
import com.wkp.hanyupinyin.presenter.callback.SpeakCallBack
import com.wkp.hanyupinyin.presenter.inter.IDetailAPresenter
import com.wkp.hanyupinyin.view.inter.IDetailAView
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.REQUEST_FLAG_CONTEXT
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_CONVERT_ERROR
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_NO_TEXT
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_PINYIN_TEXT
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_SPEAK_BEGIN
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_SPEAK_COMPLETE
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_SPEAK_ERROR
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_SPEAK_PROGRESS
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_TRANS_ERROR
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_TRANS_RESULT
import com.wkp.hanyupinyin.view.inter.IDetailAView.Companion.RESPONSE_FLAG_YOUDAO_SPEAK_RESULT

/**
 * Created by user on 2018/3/14.
 * 详情展示界面Presenter
 */
class DetailAPresenterImpl(view: IDetailAView) : IDetailAPresenter {

    private val TRANS_LANGUAGE = arrayOf("英文", "日文", "韩文", "法文", "俄文", "葡萄牙文", "西班牙文")
    val TRANS_VALUES = arrayOf(R.string.language_en, R.string.language_jp, R.string.language_kr, R.string.language_fc, R.string.language_rs,
            R.string.language_ptg, R.string.language_sp)

    var model: IDetailAModel? = null
    var view: IDetailAView? = null

    init {
        model = DetailAModelImpl()
        this.view = view
    }

    /**
     * 汉语转拼音
     */
    override fun getPinyin(chinese: String?) {
        if (chinese == null || TextUtils.isEmpty(chinese)) {
            view?.response<Throwable>(RESPONSE_FLAG_NO_TEXT, Throwable("No text!"))
            return
        }
        model?.getPinyin(chinese, object : CallBack<MutableList<Pair<String, String>>> {
            override fun onSuccess(success: MutableList<Pair<String, String>>) {
                view?.response<MutableList<Pair<String, String>>>(RESPONSE_FLAG_PINYIN_TEXT, success)
            }

            override fun onFailed(throwable: Throwable) {
                view?.response<Throwable>(RESPONSE_FLAG_CONVERT_ERROR, throwable)
            }
        })
    }

    /**
     * 有道翻译
     */
    override fun getTrans(chinese: String?) {
        if (chinese != null) {
            val context = view?.request<Context>(REQUEST_FLAG_CONTEXT)
            for (i in 0 until TRANS_LANGUAGE.size) {
                val index = i
                model?.cnToOther(chinese, TRANS_LANGUAGE[index], object : CallBack<String> {

                    override fun onSuccess(success: String) {
                        val title = context?.getString(TRANS_VALUES[index])
                        if (title != null) {
                            view?.response<TransOtherResult>(RESPONSE_FLAG_TRANS_RESULT, TransOtherResult(index,title, success,true))
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        view?.response<TransOtherResult>(RESPONSE_FLAG_TRANS_ERROR, TransOtherResult(index,context?.getString(TRANS_VALUES[index])!!, throwable.message!!,false))
                    }
                })
            }
        } else {
            view?.response<Throwable>(RESPONSE_FLAG_NO_TEXT, Throwable("No text!"))
        }
    }

    /**
     * 讯飞语音合成
     */
    override fun speaking(chinese: String?, voiceName: Int) {
        val context = view?.request<Context>(REQUEST_FLAG_CONTEXT)
        if (context != null) {
            if (chinese != null) {
                val speakProgress = SpeakProgress(voiceName, 0)
                model?.speaking(context,chinese,voiceName,object : SpeakCallBack<Int>{
                    override fun onSuccess(success: Int) {
                        speakProgress.progress = 100
                        view?.response<SpeakProgress>(RESPONSE_FLAG_SPEAK_COMPLETE, speakProgress)
                    }

                    override fun onSpeakBegin() {
                        speakProgress.progress = 0
                        view?.response<SpeakProgress>(RESPONSE_FLAG_SPEAK_BEGIN, speakProgress)
                    }

                    override fun onFailed(throwable: Throwable) {
                        view?.response<Throwable>(RESPONSE_FLAG_SPEAK_ERROR, Throwable("No text!"))
                    }

                    override fun onSpeakProgress(progress: Int) {
                        speakProgress.progress = progress
                        view?.response<SpeakProgress>(RESPONSE_FLAG_SPEAK_PROGRESS, speakProgress)
                    }

                })
            }else{
                view?.response<Throwable>(RESPONSE_FLAG_NO_TEXT, Throwable("No text!"))
            }
        }
    }

    /**
     * 有道语音合成
     */
    override fun youDaoSpeak(string: String?, langType: Int) {
        val context = view?.request<Context>(REQUEST_FLAG_CONTEXT)
        if (string != null && context != null) {
            model?.youDaoSpeak(string,langType, context,object : CallBack<Boolean>{
                override fun onSuccess(success: Boolean) {
                    view?.response<Boolean>(RESPONSE_FLAG_YOUDAO_SPEAK_RESULT, success)
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
        }
    }

}
