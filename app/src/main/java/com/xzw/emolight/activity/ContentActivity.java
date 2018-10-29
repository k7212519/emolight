package com.xzw.emolight.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.FaceSetOperate;
import com.megvii.cloud.http.Response;
import com.xzw.emolight.R;
import com.xzw.emolight.item.CardViewOne;
import com.xzw.emolight.item.CardViewThree;
import com.xzw.emolight.item.CardViewTwo;
import com.xzw.emolight.util.EmoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.xzw.emolight.R.layout.card_view_1;

public class ContentActivity extends AppCompatActivity {

    protected boolean useThemeStatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    private static String key = "FKiiXFhZOdvit2m07H0syi8HUf_1OmLz";
    private static String secret = "0A_n-uNabUOjk8wvv4bh4ZGsLE8RH9Ps";
    String return_attributes = "gender,age,smiling,facequality,emotion";

    private StringBuffer stringBuffer = new StringBuffer();
    private Bitmap bitmap;
    private byte[] imageByte=null;
    /*
    private CardViewOne cardViewOne;
    private CardViewTwo cardViewTwo;
    private CardViewThree cardViewThree;
    */

    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setUseStatusBarColor();     //设置状态栏沉浸
        initView();                 //初始化view
    }

    private void initView() {
        //初始化布局
        Button btnChangeColor = findViewById(R.id.btn_change_color);
        Button btnCapture = findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(new MyClickListener());
        btnChangeColor.setOnClickListener(new MyClickListener());
    }

    /**
     * 7212519
     * 沉浸状态栏及相关样式设置
     */
    protected void setUseStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemeStatusBarColor) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorStatusBar));
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 文件处理
     */
    public void fileHandler() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/emolpic");
        File outputImage = new File(Environment.getExternalStorageDirectory()+"/emolpic","tempImage"+".jpg");
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = FileProvider.getUriForFile(getBaseContext(), "com.chc.photo.fileProvider", outputImage);
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_capture:
                    fileHandler();//新建image文件
                    Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intentCapture, 0);
                    break;
                case R.id.btn_change_color:
                    //EmoHandler emoHandler=new EmoHandler(ContentActivity.this);
                    //String emo = emoHandler.detectFaceEmotion();
                    //Log.e("emoContent", "emo");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Resources res = ContentActivity.this.getResources();
                            Bitmap bmp= BitmapFactory.decodeResource(res, R.mipmap.c032);
                            CommonOperate commonOperate = new CommonOperate(key, secret, false);
                            //FaceSetOperate faceSetOperate = new FaceSetOperate(key, secret, false);
                            try {
                                Response response = commonOperate.detectByte(getBytes(bmp), 0, return_attributes);
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
                    break;
                default:
                    break;
            }
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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }

        public byte[] getBytes(Bitmap bitmapTemp){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapTemp.compress(Bitmap.CompressFormat.JPEG, 40,baos);
            return baos.toByteArray();
        }


    }



}
