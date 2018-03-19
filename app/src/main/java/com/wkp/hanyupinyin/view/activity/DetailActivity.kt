package com.wkp.hanyupinyin.view.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.bean.SpeakProgress
import com.wkp.hanyupinyin.model.bean.SpeakResult
import com.wkp.hanyupinyin.model.bean.TransOtherResult
import com.wkp.hanyupinyin.model.constants.IntentConstant
import com.wkp.hanyupinyin.presenter.impl.DetailAPresenterImpl
import com.wkp.hanyupinyin.presenter.inter.IDetailAPresenter
import com.wkp.hanyupinyin.view.adapter.AdapterLvSpeak
import com.wkp.hanyupinyin.view.adapter.AdapterLvTrans
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
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by user on 2018/3/7.
 * 详情展示界面
 */
class DetailActivity : BaseActivity(), IDetailAView, AdapterLvSpeak.OnSpeakClickListener, AdapterView.OnItemClickListener {

    private var presenter: IDetailAPresenter? = null
    private var mTransValues: ArrayList<TransOtherResult>? = null
    private var mSpeakValues: ArrayList<SpeakResult>? = null
    private var adapterLvSpeak: AdapterLvSpeak? = null
    private var chinese: String? = null
    val speakVoiceNames = arrayOf(R.string.voice_mandarin, R.string.voice_cantonese, R.string.voice_taiwanese, R.string.voice_lmz,
            R.string.voice_dbh, R.string.voice_he_nan, R.string.voice_hu_nan, R.string.voice_shan_xi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initView()
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        //初始化数据
        presenter = DetailAPresenterImpl(this)
        chinese = intent.getSerializableExtra(IntentConstant.INTENT_ET_TEXT) as String
        //初始化标题
        isIvBack(true)
        setTvTitle(getString(R.string.activity_hanyu_title))
        //初始化翻译界面
        mTransValues = ArrayList<TransOtherResult>()
        lv_trans_content.adapter = AdapterLvTrans(mTransValues!!)
        lv_trans_content.onItemClickListener = this
        //初始化语音合成界面
        mSpeakValues = ArrayList<SpeakResult>()
        initSpeakValues()
        adapterLvSpeak = AdapterLvSpeak(mSpeakValues!!)
        lv_sound_content.adapter = adapterLvSpeak
        adapterLvSpeak?.setOnSpeakClickListener(this)
        //初始化数据获取
        presenter?.getPinyin(chinese)
        presenter?.getTrans(chinese)
    }

    /**
     * 初始化语音合成界面
     */
    private fun initSpeakValues() {
        for (voiceName in speakVoiceNames) {
            mSpeakValues?.add(SpeakResult(getString(voiceName), 0, true))
        }
    }

    /**
     * 重置语音集合
     */
    private fun resetSpeakValues() {
        if (mSpeakValues != null) {
            for (speakResult in mSpeakValues!!) {
                speakResult.progress = 0
                speakResult.isPress = true
            }
        }
    }

    /**
     * 设置不可按
     */
    private fun setSpeakDisEnable() {
        if (mSpeakValues != null) {
            for (speakResult in mSpeakValues!!) {
                speakResult.isPress = false
            }
        }
    }

    /**
     * 语音按钮点击监听
     */
    override fun onSpeakClick(view: View, position: Int) {
        setSpeakDisEnable()
        adapterLvSpeak?.notifyDataSetChanged()
        presenter?.speaking(chinese, position)
    }

    /**
     * 翻译条目点击监听
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val transOtherResult = mTransValues?.get(position)
        if (!transOtherResult?.getSuccess()!!) {
            showToast(getString(R.string.no_useful_text))
            return
        }
        lv_trans_content.isClickable = false
        lv_trans_content.isEnabled = false
        presenter?.youDaoSpeak(transOtherResult.getContent(), transOtherResult.getPosition()!!)
    }

    /**
     * 请求
     */
    override fun <T> request(requestFlag: Int): T? {
        when (requestFlag) {
            REQUEST_FLAG_CONTEXT -> return this as T
        }
        return null
    }

    /**
     * 响应
     */
    override fun <T> response(responseFlag: Int, response: T?) {
        when (responseFlag) {
        //转拼音无文本
            RESPONSE_FLAG_NO_TEXT -> {
                tv_pinyin_content.setText(getString(R.string.no_chinese))
                tv_pinyin_content.setTextColor(Color.RED)
                tv_trans_error.visibility = View.VISIBLE
                lv_trans_content.visibility = View.GONE
            }
        //转拼音错误
            RESPONSE_FLAG_CONVERT_ERROR -> {
                tv_pinyin_content.setText(getString(R.string.convert_error))
                tv_pinyin_content.setTextColor(Color.RED)
            }
        //转拼音成功文本
            RESPONSE_FLAG_PINYIN_TEXT -> {
                tv_pinyin_content.setPinyinText(response as MutableList<Pair<String, String>>)
                tv_pinyin_content.setTextColor(Color.DKGRAY)
            }
        //翻译错误
            RESPONSE_FLAG_TRANS_ERROR -> {
                tv_trans_error.visibility = View.GONE
                lv_trans_content.visibility = View.VISIBLE
                mTransValues?.add(response as TransOtherResult)
                (lv_trans_content.adapter as AdapterLvTrans).notifyDataSetChanged()
            }
        //翻译成功
            RESPONSE_FLAG_TRANS_RESULT -> {
                tv_trans_error.visibility = View.GONE
                lv_trans_content.visibility = View.VISIBLE
                mTransValues?.add(response as TransOtherResult)
                (lv_trans_content.adapter as AdapterLvTrans).notifyDataSetChanged()
            }
        //语音合成开始
            RESPONSE_FLAG_SPEAK_BEGIN -> {
                val speakProgress = response as SpeakProgress
                if (mSpeakValues != null) {
                    mSpeakValues?.get(speakProgress.position)?.progress = speakProgress.progress
                }
                adapterLvSpeak?.notifyDataSetChanged()
            }
        //语音合成进度
            RESPONSE_FLAG_SPEAK_PROGRESS -> {
                val speakProgress = response as SpeakProgress
                if (mSpeakValues != null) {
                    mSpeakValues?.get(speakProgress.position)?.progress = speakProgress.progress
                }
                adapterLvSpeak?.notifyDataSetChanged()
            }
        //语音合成完成
            RESPONSE_FLAG_SPEAK_COMPLETE -> {
                val speakProgress = response as SpeakProgress
                if (mSpeakValues != null) {
                    mSpeakValues?.get(speakProgress.position)?.progress = speakProgress.progress
                }
                resetSpeakValues()
                adapterLvSpeak?.notifyDataSetChanged()
            }
        //语音合成错误
            RESPONSE_FLAG_SPEAK_ERROR -> {
                showToast(getString(R.string.speak_error))
                resetSpeakValues()
                adapterLvSpeak?.notifyDataSetChanged()
            }
        //有道语音合成结果
            RESPONSE_FLAG_YOUDAO_SPEAK_RESULT -> {
                lv_trans_content.isClickable = true
                lv_trans_content.isEnabled = true
                if (!(response as Boolean)) {
                    showToast(getString(R.string.speak_error))
                }
            }
        }
    }
}