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

        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnClickListener != null) {
                    titleOnClickListener.onButtonOneClick();
                }
            }
        });
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
        /**
         * 返回按钮的点击事件
         */
        void onButtonOneClick();

        /**
         * 保存按钮的点击事件
         */
        void onRightClick();

    }
}
