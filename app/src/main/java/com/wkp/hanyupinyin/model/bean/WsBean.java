package com.wkp.hanyupinyin.model.bean;

import java.util.List;

/**
 * Created by user on 2018/3/19.
 */
public class WsBean {
    /**
     * bg : 0
     * cw : [{"sc":0,"w":"We"}]
     */

    private int bg;
    private List<CwBean> cw;

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public List<CwBean> getCw() {
        return cw;
    }

    public void setCw(List<CwBean> cw) {
        this.cw = cw;
    }

}
