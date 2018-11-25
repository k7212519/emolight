package com.xzw.emolight.others;

import android.content.Context;
import android.util.Log;

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
            msgNeedToSend = "a255036000";
        } else if (emoResult == context.getString(R.string.emotion_neutral)) {
            msgNeedToSend = "a220220220";
        } else if (emoResult == context.getString(R.string.emotion_surprise)) {
            msgNeedToSend = "a255075000";
        } else if (emoResult == context.getString(R.string.emotion_sadness)) {
            msgNeedToSend = "a010010010";
        } else if (emoResult == context.getString(R.string.emotion_fear)) {
            msgNeedToSend = "a000075255";
        } else if (emoResult == context.getString(R.string.emotion_disgust)) {
            msgNeedToSend = "a000255000";
        } else if (emoResult == context.getString(R.string.emotion_anger)) {
            msgNeedToSend = "a255000000";
        } else {
            msgNeedToSend = "a000000000";
        }

        return msgNeedToSend;
    }

    /**
     * 选色盘返回的color值转需要发送的信息
     * @param color
     * @return
     */
    public String colorToMsg(int color) {
        String colorString = Integer.toHexString(color).substring(2, 8);
        int colorR = Integer.valueOf(colorString.substring(0,2), 16);
        int colorG = Integer.valueOf(colorString.substring(2, 4), 16);
        int colorB = Integer.valueOf(colorString.substring(4, 6), 16);
        String colorRstring = String.valueOf(colorR);
        String colorGstring = String.valueOf(colorG);
        String colorBstring = String.valueOf(colorB);
        if (colorRstring.length() < 3) {
            for (int i=colorRstring.length(); i < 3; i++) {
                colorRstring = "0" + colorRstring;
            }
        }

        if (colorGstring.length() < 3) {
            for (int i=colorGstring.length(); i < 3; i++) {
                colorGstring = "0" + colorGstring;
            }
        }

        if (colorBstring.length() < 3) {
            for (int i=colorBstring.length(); i < 3; i++) {
                colorBstring = "0" + colorBstring;
            }
        }
        Log.d("colorDebug", colorRstring + "+" + colorGstring + "+" + colorBstring);
        return "a" + colorRstring + colorGstring + colorBstring;
    }
}
