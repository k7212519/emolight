package com.xzw.emolight.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.xzw.emolight.R;
import com.xzw.emolight.activity.ContentActivity;
import com.xzw.emolight.others.FaceInfo;

import java.util.ArrayList;
import java.util.Collections;

public class EmotionClassifier {
    private ArrayList<Double> emoResultArrayList = new ArrayList<>(7);
//    private String originMsg;
//    private Gson gson;
    private FaceInfo faceInfo;
    public EmotionClassifier(String originMsg) {
//        this.originMsg = originMsg;
        this.faceInfo = new Gson().fromJson(originMsg, FaceInfo.class);
        //将faceInfo信息加入ArrayList
        addEmoToArray(faceInfo);
    }

    public double getEmoResultValue() {
        return Collections.max(emoResultArrayList);
    }

    public String getEmoResult(Context context) {
        //0 happiness  1 Neutral  2 Surprise  3 Sadness  4 Fear  5 Disgust  6 Anger
        int index = emoResultArrayList.indexOf(Collections.max(emoResultArrayList));
        switch (index) {
            case 0:
                return context.getString(R.string.emotion_happy);
            case 1:
                return context.getString(R.string.emotion_neutral);
            case 2:
                return context.getString(R.string.emotion_surprise);
            case 3:
                return context.getString(R.string.emotion_sadness);
            case 4:
                return context.getString(R.string.emotion_fear);
            case 5:
                return context.getString(R.string.emotion_disgust);
            case 6:
                return context.getString(R.string.emotion_anger);
            default:
                return context.getString(R.string.emotion_null);
        }
    }

    /**
     * 将情绪值添加进ArrayList
     * @param faceInfo
     */
    private void addEmoToArray(FaceInfo faceInfo) {
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getHappiness());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getNeutral());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getSurprise());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getSadness());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getFear());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getDisgust());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getAnger());
    }




}
