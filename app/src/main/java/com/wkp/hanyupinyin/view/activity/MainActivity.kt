package com.wkp.hanyupinyin.view.activity

import abc.abc.abc.nm.sp.SpotManager
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.baidu.ocr.ui.camera.CameraActivity
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.constants.IntentConstant
import com.wkp.hanyupinyin.model.constants.SPConstant
import com.wkp.hanyupinyin.model.utils.FileUtils
import com.wkp.hanyupinyin.model.utils.SPUtils
import com.wkp.hanyupinyin.presenter.impl.MainAPresenterImpl
import com.wkp.hanyupinyin.presenter.inter.IMainAPresenter
import com.wkp.hanyupinyin.view.inter.IMainAView
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.REQUEST_FLAG_CONTEXT
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_NO_BITMAP
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_NO_NETWORK
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_NO_PERMISSION
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_NO_TEXT
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_SCAN_TEXT
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_SPEECH_ERROR
import com.wkp.hanyupinyin.view.inter.IMainAView.Companion.RESPONSE_FLAG_SPEECH_TEXT
import kotlinx.android.synthetic.main.activity_base_title.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * 主界面
 */
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : BaseActivity(), IMainAView, View.OnClickListener {
    private val REQUEST_IMAGE_PICKER: Int = 900     //界面跳转 图片选取
    private val REQUEST_SCAN_TEXT: Int = 901        //界面跳转 扫描文本

    var iMainAPresenter: IMainAPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotManager.getInstance(this).onAppExit()
    }

    /**
     * 初始化控件
     */
    private fun initView() {
        iMainAPresenter = MainAPresenterImpl(this)
        setTvTitle(getString(R.string.activity_main_title))
        isTvLeft(true)
        isTvRight(true)
        setTvLeft(resources.getStringArray(R.array.language_items)[SPUtils.getInt(this, SPConstant.SP_KEY_VOICE_LANGUAGE)])
        tv_left.setOnClickListener(this)
        tv_right.setOnClickListener(this)
        iv_image.setOnClickListener(this)
        iv_voice.setOnClickListener(this)
        iv_scan.setOnClickListener(this)
        btn_sure.setOnClickListener(this)
    }

    /**
     * 点击监听
     */
    override fun onClick(v: View?) {
        when (v?.id) {
        //选择语音语言
            R.id.tv_left -> selectVoiceLanguage(MaterialDialog.ListCallbackSingleChoice { dialog, itemView, which, text ->
                SPUtils.put(dialog?.context, SPConstant.SP_KEY_VOICE_LANGUAGE, which)
                tv_left.text = text
                dialog?.dismiss()
                true
            })
        //清空编辑框
            R.id.tv_right -> et_text.setText("")
        //选择图片
            R.id.iv_image -> selectImage()
        //语音录入
            R.id.iv_voice -> startVoice()
        //扫描文本
            R.id.iv_scan -> scanText()
        //拼写发音
            R.id.btn_sure -> startDetailActivity()
        }
    }

    /**
     * 拼写发音，详情界面
     */
    private fun startDetailActivity() {
        if (TextUtils.isEmpty(et_text.text)) {
            showToast(getString(R.string.no_text))
            return
        }
        startActivity(DetailActivity::class.java, IntentConstant.INTENT_ET_TEXT, et_text.text.toString())
    }

    /**
     * 扫描文本
     */
    private fun scanText() {
        // 生成intent对象
        val intent = Intent(this, CameraActivity::class.java)
        // 设置临时存储
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtils.getSaveFile(this).absolutePath)
        // 调用除银行卡，身份证等识别的activity
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
        startActivityForResult(intent, REQUEST_SCAN_TEXT)
    }

    /**
     * 语音录入
     */
    private fun startVoice() {
        iMainAPresenter?.speech()
    }

    /**
     * 选取图片
     */
    private fun selectImage() {
        val intent = Intent(this, ImageGridActivity::class.java)
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    /**
     * 选取图片回调处理
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        //选取图片
            REQUEST_IMAGE_PICKER -> {
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null) {
                    val images: ArrayList<ImageItem> = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                    if (images.size > 0) {
                        showPbDialog(getString(R.string.please_wait))
                        iMainAPresenter?.scanText(images[0].path)
                    }
                }
            }
        //拍照选图
            REQUEST_SCAN_TEXT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    showPbDialog(getString(R.string.please_wait))
                    iMainAPresenter?.scanText(FileUtils.getSaveFile(this).absolutePath)
                }
            }
        }
    }

    /**
     * 数据请求
     */
    override fun <T> request(requestFlag: Int): T? {
        when (requestFlag) {
            REQUEST_FLAG_CONTEXT -> return this as T
        }
        return null
    }

    /**
     * 数据响应
     */
    override fun <T> response(responseFlag: Int, response: T?) {
        when (responseFlag) {
            //无权限
            RESPONSE_FLAG_NO_PERMISSION -> showToast(getString(R.string.no_permission))
            //文件非Bitmap
            RESPONSE_FLAG_NO_BITMAP -> showToast(getString(R.string.path_no_bitmap))
            //无网络
            RESPONSE_FLAG_NO_NETWORK -> showToast(getString(R.string.no_network))
            //扫描无文本
            RESPONSE_FLAG_NO_TEXT -> showToast(getString(R.string.no_useful_text))
            //语音听写错误
            RESPONSE_FLAG_SPEECH_ERROR -> showToast(getString(R.string.speech_error))
            //扫描图片文本
            RESPONSE_FLAG_SCAN_TEXT -> setEtText(response as String)
            //语音听写文本
            RESPONSE_FLAG_SPEECH_TEXT -> setEtText(response as String)
        }
        dismissPbDialog()
    }

    /**
     * 设置文本内容
     */
    private fun setEtText(response: String?) {
        val sb = StringBuilder(et_text.text)
        sb.append(response)
        et_text.setText(sb.toString())
        et_text.setSelection(sb.length)
    }

}
