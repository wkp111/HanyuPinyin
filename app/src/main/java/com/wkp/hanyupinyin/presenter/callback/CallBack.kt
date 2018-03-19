package com.wkp.hanyupinyin.presenter.callback

/**
 * Created by user on 2018/3/9.
 */
interface CallBack<T> {
    fun onSuccess(success: T)
    fun onFailed(throwable: Throwable)
}