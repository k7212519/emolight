package com.xzw.emolight.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;
import com.xzw.emolight.R;

public class CameraCaptureDialog extends DialogFragment {

    //camera属性：直接从view截取
    private int cameraMethod = CameraKit.Constants.METHOD_STILL;
    //camera属性：使用前置镜头
    private int cameraFace = CameraKit.Constants.FACING_FRONT;
    private boolean cropOutput = true;
    private CameraView camera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_camera_capture, container, false);

        camera = view.findViewById(R.id.camera_kit_in_dialog);
        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);
        camera.setFacing(cameraFace);


        return view;
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
}
