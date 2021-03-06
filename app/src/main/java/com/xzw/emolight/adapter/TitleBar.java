package com.xzw.emolight.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xzw.emolight.R;

public class TitleBar extends RelativeLayout {

    private ImageView btn1;
    private ImageView btn2;
    private ImageView btn3;

    /**
     * 标题的点击事件
     */
    private TitleOnClickListener titleOnClickListener;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        MyButtonOnClickListener myButtonOnClickListener = new MyButtonOnClickListener();
        btn1.setOnClickListener( myButtonOnClickListener);
        btn2.setOnClickListener(myButtonOnClickListener);
        btn3.setOnClickListener(myButtonOnClickListener);

    }

    /**
     * 设置标题的点击监听
     *
     * @param titleOnClickListener
     */
    public void setOnTitleClickListener(TitleOnClickListener titleOnClickListener) {
        this.titleOnClickListener = titleOnClickListener;
    }

    /**
     * 监听标题点击接口
     */
    public interface TitleOnClickListener {

        void onButtonOneClick();

        void onButtonTwoClick();

        void onButtonThreeClick();

    }

    class MyButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button1:
                    if (titleOnClickListener != null) {
                        titleOnClickListener.onButtonOneClick();
                    }
                    break;
                case R.id.button2:
                    if (titleOnClickListener != null) {
                        titleOnClickListener.onButtonTwoClick();
                    }
                    break;
                case R.id.button3:
                    if (titleOnClickListener != null) {
                        titleOnClickListener.onButtonThreeClick();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
