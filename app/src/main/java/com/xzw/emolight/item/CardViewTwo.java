package com.xzw.emolight.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xzw.emolight.R;

public class CardViewTwo extends CardView {

    private TextView textView;

    public CardViewTwo(@NonNull Context context) {
        super(context);
        /*View view = View.inflate(context, R.layout.card_view_2, null);
        addView(view);
        *//**//*textView = findViewById(R.id.text_cardView2);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("pressed");
            }
        });*/
    }

    public CardViewTwo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewTwo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
