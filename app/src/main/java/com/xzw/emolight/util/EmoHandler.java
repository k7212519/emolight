package com.xzw.emolight.util;

import android.content.Context;
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
import java.io.File;

public class EmoHandler {

    private String key = "FKiiXFhZOdvit2m07H0syi8HUf_1OmLz";
    private String secret = "0A_n-uNabUOjk8wvv4bh4ZGsLE8RH9Ps";
    private File file;
    private StringBuffer stringBuffer = new StringBuffer();
    private Context context;

    String imageUrl = "https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=29390ab34ba98226b8c12c21b2b9de3c/9a504fc2d562853527205ae798ef76c6a6ef6330.jpg";
    public EmoHandler(Context context) {
        //this.imageUrl = imageUrl;
        this.context = context;
    }

    public StringBuffer getStringBuffer() {
        return stringBuffer;
    }

    public String detectFaceEmotion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonOperate commonOperate = new CommonOperate(key, secret, false);
                FaceSetOperate faceSetOperate = new FaceSetOperate(key, secret, false);

                try {
                    Response response = commonOperate.detectByte(getBitmap(R.mipmap.c033), 0, null);
                    //Response response = commonOperate.detectUrl(imageUrl, 0, null);
                    String faceToken = getFaceToken(response);
                    //stringBuffer.append("\n");
                    stringBuffer.append(faceToken);
                    Log.e("faceAttributes",stringBuffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return stringBuffer.toString();
    }

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

    private byte[] getBitmap(int res){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
