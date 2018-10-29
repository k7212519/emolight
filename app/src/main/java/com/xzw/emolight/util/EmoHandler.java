package com.xzw.emolight.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.FaceSetOperate;
import com.megvii.cloud.http.Response;
import com.xzw.emolight.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class EmoHandler {

    private String key = "FKiiXFhZOdvit2m07H0syi8HUf_1OmLz";
    private String secret = "0A_n-uNabUOjk8wvv4bh4ZGsLE8RH9Ps";
    private String return_attributes = "gender,age,smiling,facequality,emotion";

    private StringBuffer stringBuffer = new StringBuffer();
    private String returnMsg;
    private byte[] imageByte;
    private Context context;

    String imageUrl = "https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=29390ab34ba98226b8c12c21b2b9de3c/9a504fc2d562853527205ae798ef76c6a6ef6330.jpg";
    public EmoHandler(Context context) {
        //this.imageUrl = imageUrl;
        this.context = context;
    }

    public StringBuffer getStringBuffer() {
        return stringBuffer;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public String detectFaceEmotion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonOperate commonOperate = new CommonOperate(key, secret, false);//创建新的操作
//                FaceSetOperate faceSetOperate = new FaceSetOperate(key, secret, false);
                imageByte = resToBitmap(R.drawable.c032);
                try {
                    Response response = commonOperate.detectByte(imageByte, 0, return_attributes);
//                    Response response = commonOperate.detectUrl(imageUrl, 0, null);
//                    String faceToken = getFaceToken(response);
//                    stringBuffer.append("\n");
//                    stringBuffer.append(faceToken);
                    returnMsg = new String(response.getContent());
                    Log.e("faceAttributes",stringBuffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return returnMsg;
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
    public byte[] bitMapToBytes(Bitmap bitmapTemp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapTemp.compress(Bitmap.CompressFormat.JPEG, 40,baos);
        return baos.toByteArray();
    }

}
