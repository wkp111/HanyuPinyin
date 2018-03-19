package com.wkp.hanyupinyin.model.bean;

/**
 * Created by user on 2018/3/14.
 * 语音听写翻译结果
 */

public class TransResult {

    /**
     * from : cn
     * ret : 0
     * sid : its00385a3a@ch0b1b0cac4fbc47bd00
     * to : en
     * trans_result : {"src":"今天北京的天气。","dst":"The weather in Beijing today."}
     */

    private String from;
    private int ret;
    private String sid;
    private String to;
    private TransResultBean trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public TransResultBean getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(TransResultBean trans_result) {
        this.trans_result = trans_result;
    }

    public static class TransResultBean {
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
}
