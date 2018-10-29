package com.xzw.emolight.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xzw.emolight.R;

public class TitleActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_title_bar);
    }

    @Override
    public void onClick(View v) {

    }
}
