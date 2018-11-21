package com.xzw.emolight.others;

import android.content.Context;

public class LightPlan {
    private String msgNeedToSend;
    private String emoResult;
    private double emoValue;
    private Context context;

    public LightPlan(String emoResult, double emoValue, Context context) {
        this.emoResult = emoResult;
        this.emoValue = emoValue;
        this.context = context;
    }

    public String getMsgNeedToSend() {
        return msgNeedToSend;
    }
}
