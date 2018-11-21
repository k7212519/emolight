package com.xzw.emolight.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
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
import com.github.ybq.android.spinkit.SpinKitView;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.xzw.emolight.adapter.TitleBar;
import com.xzw.emolight.dialog.CameraCaptureDialog;
import com.xzw.emolight.dialog.MyDialog;
import com.xzw.emolight.R;
import com.xzw.emolight.others.WifiControl;
import com.xzw.emolight.service.WifiService;
import com.xzw.emolight.util.EmoHandler;
import com.xzw.emolight.util.EmotionClassifier;

import java.io.File;
import java.io.IOException;

import static com.xzw.emolight.service.WifiService.ACTION_BR_SEND_MSG;


public class ContentActivity extends AppCompatActivity{

    public final static int ACTION_MESSAGE_EMOTION = 1;

    //是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;
    //是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    protected boolean useThemeStatusBarColor = false;
    //系统相机返回图片地址
    private Uri imageUri;
    //gif等待动画
    private MyDialog myDialog;
    //图像获取Dialog
    private CameraCaptureDialog cameraCaptureDialog;
    private TextView textViewReturnMsg;
    //自定义标题栏
    private TitleBar titleBar;
    //连接设备等待动画
    private ProgressWheel progressWheel;
    private int dialogImageResId = R.drawable.loading;
    private Bitmap bitmapReceived;
    //自定义的情绪处理类
    private EmoHandler emoHandler;
    //自定义的清晰分类器
    private EmotionClassifier emotionClassifier;
    //情绪百分比圆盘控件
    private SpinKitView spinKitView;
    //选色dialog
//    private ColorPickerDialog colorPickerDialog;
    //连接状态图片
    private ImageView imgDisconnect;
    private ImageView imgControl;
    //自定义的wifi控制类
    private WifiControl wifiControl;
    private Button btnControlColor;

    private Intent wifiIntent;

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
//                getImgBySys("imgBySys.jpg")
                sendMsgByWifi("a100010001000");
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(wifiIntent);
        Log.d("WifiDebug", "acDestroy");
    }

    private void initData() {
        //wifiService的intent初始化
        wifiIntent = new Intent(this, WifiService.class);
        //表情处理初始化
        emoHandler=new EmoHandler(ContentActivity.this, handler);
        //表情进度初始化
        progressWheel.setPercentage(2);
        //wifi开关控制
        wifiControl = new WifiControl(ContentActivity.this);
        //注册广播
        registerBroadcast();
    }



    /**
     * 初始化布局
     */
    private void initView() {
        MyClickListener myClickListener = new MyClickListener();
        titleBar = findViewById(R.id.title_bar);
        Button btnChangeColor = findViewById(R.id.btn_change_color);
        Button btnCapture = findViewById(R.id.btn_capture);
        btnControlColor = findViewById(R.id.btn_control_color);
        imgDisconnect = findViewById(R.id.img_connect_status);
        Button btn_search = findViewById(R.id.btn_search);
        textViewReturnMsg = findViewById(R.id.text_return_msg);
        btnCapture.setOnClickListener(myClickListener);
        btnChangeColor.setOnClickListener(myClickListener);
        btn_search.setOnClickListener(myClickListener);
        myDialog = new MyDialog(this, dialogImageResId);
        progressWheel = findViewById(R.id.wheel_progress);
        spinKitView = findViewById(R.id.spin_kit);
        imgControl = findViewById(R.id.img_color_control);
        imgControl.setOnClickListener(myClickListener);
        btnControlColor.setOnClickListener(myClickListener);
        /*colorPickerDialog = ColorPickerDialog.newBuilder()
                .setColor(getColor(R.color.colorStatusBar))
                .setDialogTitle(R.string.app_name)
                .setDialogTitle(ColorPickerDialog.TYPE_CUSTOM)
                .setShowAlphaSlider(true)
                .setDialogId(0)
                .setAllowPresets(false)
                .create();*/
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

    private void openColorPickerDialog() {
//传入的默认color
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().setColor(R.color.colorAccent)
                .setDialogTitle(R.string.app_name)
//设置dialog标题
                .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
//设置为自定义模式
                .setShowAlphaSlider(false)
//设置有透明度模式，默认没有透明度
                .setDialogId(0)
//设置Id,回调时传回用于判断
                .setAllowPresets(false)
//不显示预知模式
                .create();
//Builder创建
        colorPickerDialog.setColorPickerDialogListener(pickerDialogListener);
//设置回调，用于获取选择的颜色
        colorPickerDialog.show(this.getFragmentManager(), "color-picker-dialog");
    }

    private ColorPickerDialogListener pickerDialogListener = new ColorPickerDialogListener() {
        @Override
        public void onColorSelected(int dialogId, @ColorInt int color) {
            if (dialogId == 0) {
                //colorPickerViewModel.setColor(color);
            }
        }

        @Override
        public void onDialogDismissed(int dialogId) {

        }
    };

    //开启wifi服务
    private void startWifiService() {
        startService(wifiIntent);
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        BroadcastReceiverInContentActivity broadcastReceiverInContentActivity = new BroadcastReceiverInContentActivity();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ContentActivity.Action.ReceivedMsg");
        registerReceiver(broadcastReceiverInContentActivity, intentFilter);
    }

    /**
     * 向WifiService发送广播——发送msg
     * @param msg
     * @return
     */
    private boolean sendMsgByWifi(String msg) {
        Intent intent = new Intent();
        intent.setAction(ACTION_BR_SEND_MSG);
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
        return true;
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
                case R.id.btn_search:
                    /*if (!wifiControl.isWifiConnected()) {
                        wifiControl.OpenWifi();
                    } else {*/
                        //TODO 搜索wifi
                        startWifiService();
                        imgDisconnect.setVisibility(View.INVISIBLE);
                        spinKitView.setVisibility(View.VISIBLE);
//                    }
                    break;
                case R.id.img_color_control:
                    openColorPickerDialog();
                    break;
                case R.id.btn_control_color:
                    openColorPickerDialog();
                    break;
                default:
                    break;
            }
        }
    }

    class BroadcastReceiverInContentActivity extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case "ContentActivity.Action.ReceivedMsg":
                    break;
                case "readMsg":
                    break;
                default:
                    break;
            }
        }
    }

}