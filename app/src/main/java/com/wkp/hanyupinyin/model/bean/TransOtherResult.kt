package com.wkp.hanyupinyin.model.bean

/**
 * Created by user on 2018/3/15.
 */
class TransOtherResult() {
    private var position: Int = 0
    private var isSuccess: Boolean = false
    private var title: String? = null
    private var content: String? = null

    constructor(position: Int, title: String, content: String, isSuccess: Boolean) : this() {
        this.position = position
        this.title = title
        this.content = content
        this.isSuccess = isSuccess
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun getPosition(): Int?{
        return position
    }

    fun getTitle(): String?{
        return title
    }

    fun getContent(): String?{
        return content
    }

    fun setSuccess(isSuccess: Boolean) {
        this.isSuccess = isSuccess
    }

    fun getSuccess(): Boolean {
        return isSuccess
    }
}