package com.wkp.hanyupinyin.model.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;

/**
 * Created by wkp111 on 2017/3/1.
 * 万能的ViewHolder
 */

public class ViewHolder {
    /**
     * 返回给条目的View
     */
    public final View mConvertView;
    private static HashMap<View, Integer> mRes = new HashMap<>();
    /**
     * 私有集合存储布局中的控件
     */
    private HashMap<Integer, View> mViews = new HashMap<>();

    /**
     * 私有构造方法
     *
     * @param convertView
     */
    private ViewHolder(View convertView) {
        mConvertView = convertView;
    }

    /**
     * 对外暴露创建实例方法
     *
     * @param context
     * @param convertView
     * @param layoutRes
     * @return
     */
    public static ViewHolder newInstance(Context context, View convertView, int layoutRes) {
        if (convertView == null || mRes.get(convertView) != layoutRes) {
            convertView = LayoutInflater.from(context).inflate(layoutRes, null);
            mRes.put(convertView, layoutRes);
            convertView.setTag(new ViewHolder(convertView));
        }

        return (ViewHolder) convertView.getTag();
    }

    /**
     * 私有一个根据ID获取控件的方法
     *
     * @param id
     * @return
     */
    private View getMView(int id) {
        if (mViews.get(id) == null) {
            mViews.put(id, mConvertView.findViewById(id));
        }

        return mViews.get(id);
    }

    /**
     * 对外提供根据ID获取控件的方法
     *
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int id, Class<T> clazz) {
        return (T) getMView(id);
    }
}
