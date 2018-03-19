package com.wkp.hanyupinyin.model.bean;

/**
 * Created by user on 2018/3/15.
 */

public class SpeakProgress {
    private int position;
    private int progress;

    public SpeakProgress(int position, int progress) {
        this.position = position;
        this.progress = progress;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
