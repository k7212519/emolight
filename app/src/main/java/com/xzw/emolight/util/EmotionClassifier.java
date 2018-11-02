package com.xzw.emolight.util;

import android.util.Log;

import com.google.gson.Gson;
import com.xzw.emolight.others.FaceInfo;

import java.util.ArrayList;

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
        for (int i = 0; i < 7; i++) {
            Log.d("debug", String.valueOf(emoResultArrayList.get(i)));
        }
    }

    /*public void test() {
        String s = String.valueOf(faceInfo.getFaces().get(0).getAttributes().getEmotion().getAnger());

    }*/

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
