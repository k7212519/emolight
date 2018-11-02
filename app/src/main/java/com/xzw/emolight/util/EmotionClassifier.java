package com.xzw.emolight.util;

import android.util.Log;

import com.google.gson.Gson;
import com.xzw.emolight.others.FaceInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EmotionClassifier {
    private ArrayList<Double> emoResultArrayList = new ArrayList<>(7);
//    private String originMsg;
    private Gson gson;
    private FaceInfo faceInfo;

    public EmotionClassifier(String originMsg) {
//        this.originMsg = originMsg;
        this.faceInfo = new Gson().fromJson(originMsg, FaceInfo.class);
    }

    public void getEmoResult() {
        addEmo(faceInfo);
        /*for (int i = 0; i < 7; i++) {
            Log.d("debug", String.valueOf(emoResultArrayList.get(i)));
        }*/
        String string = Collections.max(emoResultArrayList).toString();
        //0 happiness  1 Neutral  2 Surprise  3 Sadness  4 Fear  5 Disgust  6 Anger
        int i = emoResultArrayList.indexOf(Collections.max(emoResultArrayList));
        Log.d("debug", "最大值：List"+i+"为"+string);

    }
    /**
     * 将情绪值添加进ArrayList
     * @param faceInfo
     */
    private void addEmo(FaceInfo faceInfo) {
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getHappiness());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getNeutral());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getSurprise());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getSadness());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getFear());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getDisgust());
        emoResultArrayList.add(faceInfo.getFaces().get(0).getAttributes().getEmotion().getAnger());
    }




}
