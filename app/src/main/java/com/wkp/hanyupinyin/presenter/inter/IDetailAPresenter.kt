package com.wkp.hanyupinyin.presenter.inter

/**
 * Created by user on 2018/3/14.
 */
interface IDetailAPresenter {
    fun getPinyin(chinese: String?)
    fun getTrans(chinese: String?)
    fun speaking(chinese: String?, voiceName: Int)
    fun youDaoSpeak(string: String?, langType: Int)
}