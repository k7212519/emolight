package com.xzw.emolight.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.progresviews.ProgressWheel;
import com.xzw.emolight.adapter.TitleBar;
import com.xzw.emolight.dialog.CameraCaptureDialog;
import com.xzw.emolight.dialog.MyDialog;
import com.xzw.emolight.R;
import com.xzw.emolight.item.CardViewOne;
import com.xzw.emolight.item.CardViewThree;
import com.xzw.emolight.item.CardViewTwo;
import com.xzw.emolight.others.WifiControl;
import com.xzw.emolight.service.WifiService;
import com.xzw.emolight.util.EmoHandler;
import com.xzw.emolight.util.EmotionClassifier;

import java.io.File;
import java.io.IOException;


public class ContentActivity extends AppCompatActivity{

    public final static int ACTION_MESSAGE_EMOTION = 1;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useThemeStatusBarColor = false;
    private Uri imageUri;
    private MyDialog myDialog;
    private CameraCaptureDialog cameraCaptureDialog;
    private TextView textViewReturnMsg;
    private TitleBar titleBar;
    private ProgressWheel progressWheel;
    private int dialogImageResId = R.drawable.loading;
    private Bitmap bitmapReceived;
    private EmoHandler emoHandler;
    private EmotionClassifier emotionClassifier;

    private ImageView imgDisconnect;

    /*private CardViewOne cardViewOne;
    private CardViewTwo cardViewTwo;
    private CardViewThree cardViewThree;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setUseStatusBarColor();     //设置状态栏沉浸
        initView();
        initData();

        //TODO 测试是否影响app启动速度
        startService(new Intent(this, WifiService.class));

        //title中三个按钮的事件
        titleBar.setOnTitleClickListener(new TitleBar.TitleOnClickListener() {
            @Override
            public void onButtonOneClick() {
                Toast.makeText(ContentActivity.this, "button1_clicked", Toast.LENGTH_SHORT).show();
                Log.d("debug", "button1 clicked");
                progressWheel.setPercentage(350);
            }

            @Override
            public void onButtonTwoClick() {
                startActivity(new Intent(ContentActivity.this, CameraKitActivity.class));
                Toast.makeText(ContentActivity.this, "button2_clicked", Toast.LENGTH_SHORT).show();
            }
            public void onButtonThreeClick() {
//                getImgBySys("imgBySys.jpg");
            }
        });
    }



    private void initData() {
        emoHandler=new EmoHandler(ContentActivity.this, handler);
        progressWheel.setPercentage(2);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        titleBar = findViewById(R.id.title_bar);
        Button btnChangeColor = findViewById(R.id.btn_change_color);
        Button btnCapture = findViewById(R.id.btn_capture);
        imgDisconnect = findViewById(R.id.img_connect_status);
        textViewReturnMsg = findViewById(R.id.text_return_msg);
        btnCapture.setOnClickListener(new MyClickListener());
        btnChangeColor.setOnClickListener(new MyClickListener());
        myDialog = new MyDialog(this, dialogImageResId);
        progressWheel = findViewById(R.id.wheel_progress);
        /*cardViewOne = new CardViewOne(ContentActivity.this);
        cardViewTwo = new CardViewTwo(ContentActivity.this);
        cardViewThree = new CardViewThree(ContentActivity.this);*/



    }

    /**
     * 正确的handler写法，错误示例
     * private Handler mHandler = new Handler(new Handler.Callback() {
     *         public boolean handleMessage(Message msg) {
     *             return false;
     *         }
     *     });
     */
    private Handler handler= new Handler(new Handler.Callback() {
        public boolean handleMessage(Message message){
            switch (message.what) {
                case ACTION_MESSAGE_EMOTION:
                    String emo = message.getData().getString("returnEmoMsg");

                    //TODO 调用系统相机会闪退，需要判断cameraCapture状态
                    cameraCaptureDialog.dismiss();

                    //myDialog.cancel();
                    textViewReturnMsg.setText(emo);
                    Log.d("debug",emo);

                    //解析emo数据
                    emotionClassifier = new EmotionClassifier(emo);
                    emotionClassifier.getEmoResult(ContentActivity.this);
                    Log.d("debug", emotionClassifier.getEmoResult(ContentActivity.this));
                    setProgressWheelByEmo(emotionClassifier.getEmoResultValue(),
                            emotionClassifier.getEmoResult(ContentActivity.this));
                    Log.d("debug", String.valueOf(emotionClassifier.getEmoResultValue()));
                    break;
                case 2:
                    /*if (TitleFragment.loadingImageSelect == 0) {
                        dialogImageResId = R.drawable.loading;
                    } else if (TitleFragment.loadingImageSelect == 1) {
                        dialogImageResId = R.drawable.loading_duck;
                    } else {
                        dialogImageResId = R.drawable.loading;
                    }*/
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
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
        //android6.0以后可以对状态栏文字颜色和图标进行修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 调用系统相机获取图片 存到imageUri
     */
    private void getImgBySys(String imgFileName) {
        //新建image文件
        File dir = new File(Environment.getExternalStorageDirectory() + "/emolpic");
        File outputImage = new File(Environment.getExternalStorageDirectory()+"/emolpic",imgFileName);
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
        //将activity返回的文件存入imageUri
        Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCapture, 0);
    }

    private void setProgressWheelByEmo(double emoValue, String emoType) {
        if (emoValue > 99) {
            progressWheel.setPercentage(360);
        }else if (emoValue > 90) {
            progressWheel.setPercentage(340);
        } else if (emoValue <= 90) {
            progressWheel.setPercentage(340/90 * (int)emoValue);
        }

        progressWheel.setStepCountText(emoType);
        progressWheel.setDefText(getString(R.string.reliability_text)+String.valueOf((int) emoValue)+"%");
    }

    private void startWifiService() {

    }

    /**
     * cardview1 按键响应
     */
    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_capture:
                    cameraCaptureDialog = new CameraCaptureDialog();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    //设置dialogFragment进场动画
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    //对cameraCaptureDialog设置监听
                    cameraCaptureDialog.setOnCaptureDialogFragmentListener(
                            new CameraCaptureDialog.OnCaptureDialogFragmentListener() {
                                @Override
                                public void onHandleBitmap(Bitmap bitmapTmp) {
                                    Log.d("debug", "received");
                                    bitmapReceived = bitmapTmp;
                                    if (bitmapReceived != null) {
                                        //将接收到的bitmap文件存入sdcard/emolpic/faceIng.jpg
                                        emoHandler.createFile(bitmapReceived,
                                                Environment.getExternalStorageDirectory()+"/emolpic/"+"faceImg.jpg");
                                        //从sdcard加载文件进行解析，通过handler解析返回值
                                        emoHandler.detectFaceEmotion(
                                                Environment.getExternalStorageDirectory()+"/emolpic/"+"faceImg.jpg", 180);
                                    }
                                }
                            });
                    cameraCaptureDialog.show(fragmentTransaction, "cameraCapDialog");
                    break;
                case R.id.btn_change_color:
                    //FaceInfo faceInfo = new FaceInfo();
                    emoHandler.detectFaceEmotion(Environment.getExternalStorageDirectory()+"/emolpic/"+"imgBySys.jpg", 90);
                    //Log.d("debug", emo);
                    myDialog.show();
                    break;
                default:
                    break;
            }
        }
    }

}