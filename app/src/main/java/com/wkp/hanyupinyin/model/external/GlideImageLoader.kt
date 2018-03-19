package com.wkp.hanyupinyin.model.external

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lzy.imagepicker.loader.ImageLoader
import java.io.File

/**
 * Created by user on 2018/3/9.
 * 图片加载器
 */
class GlideImageLoader : ImageLoader {
    /**
     * 图片选择框
     */
    override fun displayImage(activity: Activity?, path: String?, imageView: ImageView?, width: Int, height: Int) {
        val requestOptions = RequestOptions.overrideOf(width, height)
        if (activity != null && imageView != null) {
            Glide.with(activity)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Uri.fromFile(File(path)))
                    .into(imageView)
        };
    }

    /**
     * 图片预览框
     */
    override fun displayImagePreview(activity: Activity?, path: String?, imageView: ImageView?, width: Int, height: Int) {
        val requestOptions = RequestOptions.overrideOf(width, height)
        if (activity != null && imageView != null) {
            Glide.with(activity)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Uri.fromFile(File(path)))
                    .into(imageView)
        }
    }

    override fun clearMemoryCache() {
    }
}