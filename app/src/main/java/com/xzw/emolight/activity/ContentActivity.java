package com.xzw.emolight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.xzw.emolight.dialog.MyDialog;
import com.xzw.emolight.R;
//import com.xzw.emolight.others.FaceInfo;
import com.xzw.emolight.util.EmoHandler;

import java.io.File;
import java.io.IOException;


public class ContentActivity extends AppCompatActivity {

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useThemeStatusBarColor = false;
    private TimeHandler timeHandler = new TimeHandler();
    private Uri imageUri;
    private MyDialog myDialog;
    private int progress = 0;
    private String[] MESSAGES = {"加载中","加载中.","加载中..","加载中..."};
    /*
    private CardViewOne cardViewOne;
    private CardViewTwo cardViewTwo;
    private CardViewThree cardViewThree;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setUseStatusBarColor();     //设置状态栏沉浸
        initView();                 //初始化view
    }

    /**
     * 初始化布局
     */
    private void initView() {
        Button btnChangeColor = findViewById(R.id.btn_change_color);
        Button btnCapture = findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(new MyClickListener());
        btnChangeColor.setOnClickListener(new MyClickListener());
        myDialog = new MyDialog(this);

        myDialog.setMessage(this.getString(R.string.loading));
        timeHandler.sendEmptyMessage(1);

    }



    private void showDialog(){
        myDialog.show();
    }

    private Handler handler= new Handler() {
        public void handleMessage(Message message){
            switch (message.what) {
                case 1:
                    String emo = message.getData().getString("returnMsg");
                    myDialog.cancel();
                    Log.d("debug",emo);
                    break;
                default:
                    break;
            }
        };
    };

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
                    //FaceInfo faceInfo = new FaceInfo();
                    EmoHandler emoHandler=new EmoHandler(ContentActivity.this, handler);
                    emoHandler.detectFaceEmotion();
                    //Log.d("debug", emo);
                    showDialog();
                    progress = 0;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 时间处理类
     */
    class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (progress < 1000){
                        progress += 20;
                        myDialog.setProgress(progress);
                        myDialog.setMessage(MESSAGES[progress / 20 % 4]);

                    }else {
                        progress = 0;
                    }
                    sendEmptyMessageDelayed(1,500);
                    break;
            }
        }
    }
}