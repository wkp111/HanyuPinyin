package com.wkp.hanyupinyin.model.inter

import android.content.Context
import android.util.Pair
import com.wkp.hanyupinyin.presenter.callback.CallBack
import com.wkp.hanyupinyin.presenter.callback.SpeakCallBack

/**
 * Created by user on 2018/3/14.
 */
interface IDetailAModel {
    fun getPinyin(hanYu: String, callBack: CallBack<MutableList<Pair<String,String>>>)
    fun cnToOther(chinese: String,other: String, callBack: CallBack<String>)
    fun speaking(context: Context, chinese: String, voiceName: Int, callBack: SpeakCallBack<Int>)
    fun youDaoSpeak(string: String, langType: Int, context: Context, callBack: CallBack<Boolean>)
}