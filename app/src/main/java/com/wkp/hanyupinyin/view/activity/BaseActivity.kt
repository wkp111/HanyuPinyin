package com.wkp.hanyupinyin.view.activity

import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.constants.SPConstant
import com.wkp.hanyupinyin.model.utils.CommonUtils
import com.wkp.hanyupinyin.model.utils.SPUtils
import kotlinx.android.synthetic.main.activity_base_title.*
import java.io.Serializable


/**
 * Created by user on 2018/3/7.
 * Activity基类
 */
open class BaseActivity : AppCompatActivity() {
    var mDialog: SweetAlertDialog? = null

    /**
     * 是否开启返回键
     */
    fun isIvBack(isIvBack: Boolean) {
        iv_back?.visibility = if (isIvBack) View.VISIBLE else View.GONE
        tv_left?.visibility = if (isIvBack) View.GONE else View.VISIBLE
        iv_back?.setOnClickListener{
            finish()
        }
    }

    /**
     * 是否开启左侧文本
     */
    fun isTvLeft(isTvLeft: Boolean) {
        tv_left?.visibility = if (isTvLeft) View.VISIBLE else View.GONE
        iv_back?.visibility = if (isTvLeft) View.GONE else View.VISIBLE
    }

    /**
     * 设置左侧文本
     */
    fun setTvLeft(left: String) {
        tv_left?.text = left
    }

    /**
     * 是否开启右侧文本
     */
    fun isTvRight(isTvRight: Boolean) {
        tv_right?.visibility = if (isTvRight) View.VISIBLE else View.GONE
    }

    /**
     * 设置标题文本
     */
    fun setTvTitle(title: String) {
        tv_title?.text = title
    }

    /**
     * Toast
     */
    fun showToast(text: String) {
        CommonUtils.showToast(this, text)
    }

    /**
     * 跳转界面
     */
    fun startActivity(aClass: Class<out Activity>, key: String, value: Serializable) {
        CommonUtils.startActivity(this, aClass, key, value)
    }

    /**
     * 进度对话框show
     */
    fun showPbDialog(text: String) {
        if (mDialog == null) {
            mDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        }
        if (mDialog?.isShowing!!) {
            mDialog?.dismiss()
        }
        mDialog?.progressHelper?.barColor = Color.parseColor("#2d90ec")
        mDialog?.titleText = text
        mDialog?.setCancelable(false)
        mDialog?.setCanceledOnTouchOutside(false)
        mDialog?.show()
    }

    /**
     * 进度对话框dismiss
     */
    fun dismissPbDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog?.dismiss()
        }
    }

    /**
     * 语音听写选择框
     */
    fun selectVoiceLanguage(callBack: MaterialDialog.ListCallbackSingleChoice) {
        val index = SPUtils.getInt(this, SPConstant.SP_KEY_VOICE_LANGUAGE)
        MaterialDialog.Builder(this)
                .title(R.string.select_voice_language)
                .items(R.array.language_items)
                .itemsCallbackSingleChoice(index, callBack)
                .show()
    }
}