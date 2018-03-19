package com.wkp.hanyupinyin.model.inter

import android.content.Context
import com.wkp.hanyupinyin.presenter.callback.CallBack

/**
 * Created by user on 2018/3/8.
 */
interface IMainAModel {
    fun scanTextBaiDu(filePath: String, containsLocation: Boolean, callBack: CallBack<String>)
    fun speech(context: Context?,callBack: CallBack<String>)
}