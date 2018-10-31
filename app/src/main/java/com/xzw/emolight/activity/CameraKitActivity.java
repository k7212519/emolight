package com.xzw.emolight.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;
import com.xzw.emolight.R;

public class CameraKitActivity extends Activity{

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;

    Button button;

    private CameraView camera;
    private boolean cropOutput = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);


        //下次使用butterknife框架
        camera = findViewById(R.id.camera_kit);
        //设置camera属性
        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);
        button = findViewById(R.id.btn_cap);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                    @Override
                    public void callback(CameraKitImage cameraKitImage) {
                        imageCaptured(cameraKitImage);
                    }
                });
            }
        });
    }

    @Nullable

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.stop();
    }

    public void imageCaptured(CameraKitImage cameraKitImage) {
        Bitmap bitmap = cameraKitImage.getBitmap();
        Toast.makeText(CameraKitActivity.this, "imageDetected", Toast.LENGTH_SHORT).show();
    }
}
