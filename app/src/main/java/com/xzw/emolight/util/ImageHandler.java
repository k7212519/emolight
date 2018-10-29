package com.xzw.emolight.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import com.xzw.emolight.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;


public class ImageHandler {


    private static final int IMAGE_SIZE = 48;
    //private Classifier classifier;//识别类
    //private CascadeClassifier cascadeClassifier = null; //级联分类器
    //private ImageView imageView;
    //private static final String MODEL_FILE = "file:///android_asset/FacialExpressionReg.pb";


    /**
     * 图像灰度归一化
     * @param bmpOriginal
     * @return
     */
    public static Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 图像大小归一化
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public Bitmap scaleImage(Bitmap bitmap, int width, int height)
    {

        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Imgproc.resize(src, dst, new Size(width,height),0,0,Imgproc.INTER_AREA);
        Bitmap bitmap1 = Bitmap.createBitmap(dst.cols(),dst.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmap1);
        return bitmap1;
    }

    /**
     * 获取输入像素集
     * Android不支持单通道图片
     * @param bitmap
     * @return
     */
    public float[] getSingleChannelPixel(Bitmap bitmap) {
        float[] floatValues = new float[IMAGE_SIZE * IMAGE_SIZE * 1];

        if ((bitmap.getWidth() != IMAGE_SIZE) ||  (bitmap.getHeight() != IMAGE_SIZE)){
            Log.d("getSingleChannelPixel","获取像素时图片尺寸不对");
        }

        StringBuffer sBuffer = new StringBuffer("像素值：");
        for(int i = 0;i<bitmap.getWidth();i++)
        {
            for(int j =0;j<bitmap.getHeight();j++)
            {
                int col = bitmap.getPixel(i, j);
                int alpha = col&0xFF000000;
                int red = (col&0x00FF0000)>>16;
                int green = (col&0x0000FF00)>>8;
                int blue = (col&0x000000FF);
                int gray = (int)((float)red*0.3+(float)green*0.59+(float)blue*0.11);
                //int newColor = alpha|(gray<<16)|(gray<<8)|gray;
                floatValues[i + j* IMAGE_SIZE] = gray / 255.0f;
                sBuffer.append(gray) ;
                sBuffer.append(" ") ;
            }
        }
        //putStringToTxt(sBuffer.toString(), "pixel");
        return floatValues;
    }


    /**
     * 图像旋转
     * @param bm
     * @param orientationDegree
     * @return
     */
    public Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
    {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /**
     * 使用级联分类器查找face
     * @param bitmap
     */
    /*
    private void detectFace(Bitmap bitmap)
    {

        Mat img = new Mat();
        Utils.bitmapToMat(bitmap, img);

        Mat imgGray = new Mat();;
        MatOfRect faces = new MatOfRect();

        if(img.empty())
        {
            Log.d("ccx","detectFace but img is empty");
            return;
        }

        if(img.channels() ==3)
        {
            Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_RGB2GRAY);
        }
        else
        {
            imgGray = img;
        }

        cascadeClassifier.detectMultiScale(imgGray, faces, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0){
            for (int i = 0; i < facesArray.length; i++) {
                Imgproc.rectangle(imgGray, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
                Log.d("ccx","index:" + i + "topLeft:" + facesArray[i].tl() + "bottomRight:" + facesArray[i].br()+ "height:" + facesArray[i].height);
            }
        }

        Utils.matToBitmap(imgGray, bitmap);
        imageView.setImageBitmap(bitmap);
        Bitmap destBitmap = Bitmap.createBitmap(bitmap, (int) (facesArray[0].tl().x), (int) (facesArray[0].tl().y), facesArray[0].width, facesArray[0].height);
        Bitmap scaleImage = scaleImage(destBitmap, 48, 48);
        Bitmap bitmap5 = toGrayscale(scaleImage);
        Bitmap bitmap6 = adjustPhotoRotation(bitmap5, 270);

        classifier = new Classifier(getAssets(),MODEL_FILE);
        ArrayList<String> result = classifier.predict(getSingleChannelPixel(bitmap6));
        //0=Angry, 1=Disgust, 2=Fear, 3=Happy, 4=Sad, 5=Surprise, 6=Neutral
        String str = result.get(0);
        switch(str){
            case "0":
                str = "生气";break;
            case "1":
                str = "厌恶";break;
            case "2":
                str = "恐惧";break;
            case "3":
                str = "开心";break;
            case "4":
                str = "难过";break;
            case "5":
                str = "惊讶";break;
            case "6":
                str = "平静";break;
            default:
                Log.d("ccx","Tensorflow return is error.");;break;
        }
        reslutTextView.setText("识别结果: " + str);
        return;
    }
    */
}
