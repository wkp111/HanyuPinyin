package com.wkp.hanyupinyin.model.bean;

/**
 * Created by user on 2018/3/19.
 */
public class TransResultBean {
    /**
     * src : 今天北京的天气。
     * dst : The weather in Beijing today.
     */

    private String src; //原文
    private String dst; //译文

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
