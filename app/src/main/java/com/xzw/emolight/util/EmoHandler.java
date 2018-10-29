package com.xzw.emolight.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;
import com.xzw.emolight.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class EmoHandler {

    public static final int UPDATE_UI = 1;

    private String key = "FKiiXFhZOdvit2m07H0syi8HUf_1OmLz";
    private String secret = "0A_n-uNabUOjk8wvv4bh4ZGsLE8RH9Ps";
    private String return_attributes = "gender,age,smiling,facequality,emotion";

    private byte[] imageByte;
    private Context context;
    //private FaceInfo faceInfo;
    private Handler handler;
    private FileInputStream fileInputStream;
    public EmoHandler(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void detectFaceEmotion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonOperate commonOperate = new CommonOperate(key, secret, false);//创建新的操作
                //imageByte = resToBitmap(R.drawable.c032);
                try {
                    fileInputStream = new FileInputStream("/sdcard/emolpic/tempImage.jpg");
                    Bitmap bitmap  = BitmapFactory.decodeStream(fileInputStream);
                    imageByte = bitmapToBytes(bitmap);
                    Response response = commonOperate.detectByte(imageByte, 0, return_attributes);
                    String returnMsg = new String(response.getContent());

                    //通过handler+bundle+message向主线程传递消息，UI事件在主线程中处理
                    Message message = handler.obtainMessage();
                    message.what = UPDATE_UI;
                    Bundle bundle = new Bundle();
                    bundle.putString("returnMsg",returnMsg);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 获得faceToken
     * @param response
     * @return
     * @throws JSONException
     */
    private String getFaceToken(Response response) throws JSONException {
        if(response.getStatus() != 200){
            return new String(response.getContent());
        }
        String res = new String(response.getContent());
        Log.e("response", res);
        JSONObject json = new JSONObject(res);
        String faceToken = json.optJSONArray("faces").optJSONObject(0).optString("face_token");
        return faceToken;
    }

    /**
     * 图片资源转byte[]
     * @param res
     * @return
     */
    private byte[] resToBitmap(int res){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * bitmap转byte[]
     * @param bitmapTemp
     * @return
     */
    public byte[] bitmapToBytes(Bitmap bitmapTemp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapTemp.compress(Bitmap.CompressFormat.JPEG, 40,baos);
        return baos.toByteArray();
    }

    /**
     private String getFaceEmotion(String attriButeStr) throws JSONException {

     JSONObject json = new JSONObject(attriButeStr);
     String emotion = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optString("emotion");
     happiness = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("happiness");
     sadness = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("sadness");
     neutral = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("neutral");
     disgust = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("disgust");
     anger = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("anger");
     surprise = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("surprise");
     fear = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("emotion").optDouble("fear");
     age = json.optJSONArray("faces").optJSONObject(0).optJSONObject("attributes").optJSONObject("age").optInt("value");
     Intent intentSendMessage = new Intent();

     if (age != 0) {
     Intent intent = new Intent();
     intent.setAction("updateEmotionText");
     sendBroadcast(intent);
     if (switchStatus == true) {
     if (happiness > 50) {
     textViewEmotion.setText("开心");
     //if ( emotionColor!=1 ){
     emotionColor = 1;
     Log.e("tag", "happy大于50");
     sendIntMessage(intentSendMessage, 1, 1);
     //}
     } else if (neutral > 70 || sadness > 30 || disgust > 30) {
     if (neutral > 60)
     textViewEmotion.setText("平静");
     else if (sadness > 60)
     textViewEmotion.setText("低落");
     else if (disgust > 60)
     textViewEmotion.setText("低落");
     //if (emotionColor != 3){
     emotionColor = 3;
     sendIntMessage(intentSendMessage, 1, 3);
     //}

     } else if (surprise > 30 || anger > 30 || fear > 30) {
     if (surprise > 30)
     textViewEmotion.setText("惊讶");
     else if (anger > 30)
     textViewEmotion.setText("愤怒");
     else if (fear > 30)
     textViewEmotion.setText("害怕");
     //if (emotionColor!=2) {
     emotionColor = 2;
     sendIntMessage(intentSendMessage, 1, 2);
     //}
     } else {
     //if (emotionColor!=1){
     //emotionColor = 1;
     //sendIntMessage(intentSendMessage,1,1);
     //}
     }
     }

     }
     Log.e("age",String.valueOf(age));
     Log.e("happiness",String.valueOf(happiness));



     return emotion;
     }
     **/

}
