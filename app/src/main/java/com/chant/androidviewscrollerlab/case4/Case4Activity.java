package com.chant.androidviewscrollerlab.case4;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import com.chant.androidviewscrollerlab.R;

/**
 * View的滚动（touch事件控制滚动）
 */
public class Case4Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.case4_layout);

        bindEvent();
    }

    private void bindEvent() {
        final Case4ViewGroup containerViewGroup = (Case4ViewGroup) findViewById(R.id.container_viewgroup);
        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
    }


}
