package com.wkp.hanyupinyin.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.bean.SpeakResult
import com.wkp.hanyupinyin.model.utils.ViewHolder
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

/**
 * Created by user on 2018/3/15.
 * 讯飞语音合成列表适配器
 */
class AdapterLvSpeak(values: MutableList<SpeakResult>): BaseAdapter() {
    var values: MutableList<SpeakResult>? = null
    private var listener: OnSpeakClickListener? = null

    init {
        this.values = values
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = ViewHolder.newInstance(parent?.context, convertView, R.layout.item_lv_speak)
        val speakResult = values?.get(position)
        holder.getView(R.id.tv_voice_name,TextView::class.java).text = speakResult?.voiceName
        holder.getView(R.id.pb_voice_speak,MaterialProgressBar::class.java).progress = speakResult?.progress!!
        val ivSpeak = holder.getView(R.id.iv_voice_speak, ImageView::class.java)
        ivSpeak.isClickable = speakResult.isPress
        ivSpeak.isEnabled = speakResult.isPress
        ivSpeak.setOnClickListener {
            if (listener != null) {
                listener?.onSpeakClick(it,position)
            }
        }
        return holder.mConvertView
    }

    override fun getItem(position: Int): Any {
        return values?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (values == null) 0 else values?.size!!
    }

    interface OnSpeakClickListener{
        fun onSpeakClick(view: View, position: Int)
    }

    fun setOnSpeakClickListener(listener: OnSpeakClickListener){
        this.listener = listener
    }
}