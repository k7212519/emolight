package com.xzw.emolight.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xzw.emolight.R;
import com.xzw.emolight.activity.ContentActivity;

public class CardViewOne extends CardView {

    private ImageView imageView;

    public CardViewOne(@NonNull Context context) {
        super(context);
        //imageView = imageView.findViewById(R.id.image_card_one);
        //imageView.setColorFilter(R.color.colorBackground);
    }

    public CardViewOne(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewOne(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
