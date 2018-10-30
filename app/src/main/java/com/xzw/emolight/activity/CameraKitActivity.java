package com.xzw.emolight.activity;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;
import com.xzw.emolight.R;

public class CameraKitActivity extends Activity{

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;
    private CameraView cameraView;
    private boolean cropOutput = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);


        //下次使用butterknife框架
        cameraView = findViewById(R.id.camera_kit);
        //设置camera属性
        cameraView.setMethod(cameraMethod);
        cameraView.setCropOutput(cropOutput);
    }

    @Nullable

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
        cameraView.captureImage();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.stop();
    }
}
