package com.wkp.hanyupinyin.view.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wkp.hanyupinyin.R
import com.wkp.hanyupinyin.model.bean.TransOtherResult
import com.wkp.hanyupinyin.model.utils.ViewHolder

/**
 * Created by user on 2018/3/15.
 * 翻译列表适配器
 */
class AdapterLvTrans(values: MutableList<TransOtherResult>): BaseAdapter() {

    var mValues: MutableList<TransOtherResult>? = null

    init {
        this.mValues = values
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder = ViewHolder.newInstance(parent?.context, convertView, R.layout.item_lv_trans)
        val transResult = mValues?.get(position)
        holder.getView(R.id.tv_trans_title,TextView::class.java).visibility = if (transResult != null) View.VISIBLE else View.GONE
        holder.getView(R.id.tv_trans_content,TextView::class.java).visibility = if (transResult != null) View.VISIBLE else View.GONE
        if (transResult != null) {
            holder.getView(R.id.tv_trans_title,TextView::class.java).text = transResult.getTitle()
            holder.getView(R.id.tv_trans_content,TextView::class.java).text = transResult.getContent()
            holder.getView(R.id.tv_trans_content,TextView::class.java).setTextColor(if (transResult.getSuccess()) Color.DKGRAY else Color.RED)
        }
        return holder.mConvertView
    }

    override fun getItem(position: Int): TransOtherResult? {
        return mValues?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return if (mValues == null) 0 else mValues?.size!!
    }
}