package com.xzw.emolight.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;
import com.xzw.emolight.R;

public class CameraCaptureDialog extends DialogFragment {

    private static int ACTION_START = 0;
    private static int ACTION_END = 1;
    //camera属性：直接从view截取
    private int cameraMethod = CameraKit.Constants.METHOD_STILL;
    //camera属性：使用前置镜头
    private int lastCamera = CameraKit.Constants.FACING_FRONT;
    private int cameraFace = CameraKit.Constants.FACING_FRONT;
    private int cameraBack = CameraKit.Constants.FACING_BACK;
    private boolean cropOutput = true;
    private ImageView btnScanImgFace;
    private ImageView btnScanImgFrame;
    private Button buttonScan;
    private Button buttonSwitchCamera;
    private RelativeLayout layoutScan;
    private CameraView camera;
    private ImageView imageViewScanLine;

    private Bitmap bitmap;
    //接口类，用来传递bitmap到activity
    public OnCaptureDialogFragmentListener onCaptureDialogFragmentListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.fragment_dialog);
    }

    //消除setOnTouchListener警告
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_camera_capture, container, false);
        imageViewScanLine = view.findViewById(R.id.img_scan_line);
        buttonScan = view.findViewById(R.id.btn_scan);
        layoutScan = view.findViewById(R.id.layout_scan);
        btnScanImgFace = view.findViewById(R.id.img_scan_btn_face);
        btnScanImgFrame = view.findViewById(R.id.img_scan_btn_frame);
        buttonSwitchCamera = view.findViewById(R.id.btn_switch_camera);
        camera = view.findViewById(R.id.camera_kit_in_dialog);
        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);
        camera.setFacing(lastCamera);

        /**
         * 设置dialog不响应空白处
         */
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        controlFrameAnim(btnScanImgFrame, ACTION_START);


        btnScanImgFace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        controlLineAnim(imageViewScanLine, ACTION_START);
                        controlFrameAnim(btnScanImgFrame, ACTION_END);

                        //震动11ms
                        Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                        vibrator.vibrate(11);

                        //从camera获取bitmap用接口回调给activity
                        camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                            @Override
                            public void callback(CameraKitImage cameraKitImage) {
                                //返回CameraKitImage类型文件，使用imageCaptured()方法转bitmap
                                bitmap = cameraKitImage.getBitmap();

                                //前后摄像头拍的照片方向相差180
                                if (lastCamera == cameraBack) {
                                    Matrix matrix = new Matrix();
                                    matrix.setRotate(180);
                                    Bitmap bitmapRotate = Bitmap.createBitmap(
                                            bitmap, 0, 0, bitmap.getWidth(),
                                            bitmap.getHeight(), matrix, false);

                                    //获取byte[]类型数据
//                                imageBytes = cameraKitImage.getJpeg();
                                    //传递bitmapRotate
                                    onCaptureDialogFragmentListener.onHandleBitmap(bitmapRotate);
                                } else {
                                    onCaptureDialogFragmentListener.onHandleBitmap(bitmap);
                                }


                            }
                        });




                        break;
                    case MotionEvent.ACTION_UP:
                        controlFrameAnim(btnScanImgFrame, ACTION_START);
                        controlLineAnim(imageViewScanLine, ACTION_END);
                        break;
                }
                //返回值含义暂时不理解
                return true;
            }
        });

        /**
         * 切换镜头
         */
        buttonSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera.isFacingFront()) {
                    camera.setFacing(cameraBack);
                    lastCamera = cameraBack;
                } else if (camera.isFacingBack()) {
                    camera.setFacing(cameraFace);
                    lastCamera = cameraFace;
                }
            }
        });
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


    /**
     * 扫描线动画控制
     *
     * @param view
     */
    private void controlLineAnim(View view, int acton) {
        Animation animationLineTranslate = new TranslateAnimation(
                0, 0, 0, layoutScan.getHeight() * 0.64f);
        animationLineTranslate.setRepeatMode(Animation.REVERSE);
        animationLineTranslate.setRepeatCount(10000);
        animationLineTranslate.setDuration(1000);
        if (acton == ACTION_START) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animationLineTranslate);
        } else if (acton == ACTION_END) {
            view.clearAnimation();
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 扫描button框默认动画
     *
     * @param view
     * @param action
     */
    private void controlFrameAnim(View view, int action) {
        Animation animationFrameScale = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationFrameScale.setRepeatCount(10000);
        animationFrameScale.setRepeatMode(Animation.REVERSE);
        animationFrameScale.setDuration(300);
        if (action == ACTION_START) {
            view.startAnimation(animationFrameScale);
        } else if (action == ACTION_END) {
            view.clearAnimation();
        }
    }

    /**
     * bitmap的传递接口，activity实现接口，重写onHandleBitmap
     */
    public interface OnCaptureDialogFragmentListener{
        void onHandleBitmap(Bitmap bitmapTmp);
    }


    public void setOnCaptureDialogFragmentListener(OnCaptureDialogFragmentListener onCaptureDialogFragmentListener) {
        this.onCaptureDialogFragmentListener = onCaptureDialogFragmentListener;
    }



}
