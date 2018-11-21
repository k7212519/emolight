package com.xzw.emolight.others;

import android.content.Context;

import com.xzw.emolight.R;

public class LightPlan {
    private String msgNeedToSend;
    private String emoResult;
    private double emoValue;
    private Context context;

    public LightPlan(Context context) {
        this.context = context;
    }

    public String getMsgNeedToSend(String emoResult, double emoValue) {
        //0 happiness  1 Neutral  2 Surprise  3 Sadness  4 Fear  5 Disgust  6 Anger
        //happiness 橙色 r255*4 g150 b0
        //Neutral   白色 r255*4 g255*4 b255*4
        //Surprise  黄色 r255*4 g300 b0
        //Sadness   暗光 r50 g50 b50
        //Fear      蓝白 r0 g300 b255*4
        //Disgust   绿色 r0 g1020 b0
        //Anger     红色 r255*4

        if (emoResult == context.getString(R.string.emotion_happy)){
            msgNeedToSend = "a102001500000";
        } else if (emoResult == context.getString(R.string.emotion_neutral)) {
            msgNeedToSend = "a080008000800";
        } else if (emoResult == context.getString(R.string.emotion_surprise)) {
            msgNeedToSend = "a102003000000";
        } else if (emoResult == context.getString(R.string.emotion_sadness)) {
            msgNeedToSend = "a005000500050";
        } else if (emoResult == context.getString(R.string.emotion_fear)) {
            msgNeedToSend = "a000003001020";
        } else if (emoResult == context.getString(R.string.emotion_disgust)) {
            msgNeedToSend = "a000010200000";
        } else if (emoResult == context.getString(R.string.emotion_anger)) {
            msgNeedToSend = "a102000000000";
        } else {
            msgNeedToSend = "a000000000000";
        }

        return msgNeedToSend;
    }
}
