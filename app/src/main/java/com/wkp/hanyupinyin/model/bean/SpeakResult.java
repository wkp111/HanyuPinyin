package com.wkp.hanyupinyin.model.bean;

/**
 * Created by user on 2018/3/15.
 */

public class SpeakResult {
    private String voiceName;
    private int progress;
    private boolean press;

    @Override
    public String toString() {
        return "SpeakResult{" +
                "voiceName='" + voiceName + '\'' +
                ", progress=" + progress +
                ", press=" + press +
                '}';
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }

    public SpeakResult(String voiceName, int progress, boolean press) {

        this.voiceName = voiceName;
        this.progress = progress;
        this.press = press;
    }

    public SpeakResult() {

    }
}
