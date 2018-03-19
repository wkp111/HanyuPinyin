package com.wkp.hanyupinyin.presenter.callback

/**
 * Created by user on 2018/3/15.
 */
interface SpeakCallBack<T>: CallBack<T> {
    fun onSpeakBegin()
    fun onSpeakProgress(progress: Int)
}