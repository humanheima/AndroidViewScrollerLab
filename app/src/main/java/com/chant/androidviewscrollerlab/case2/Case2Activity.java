package com.chant.androidviewscrollerlab.case2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.chant.androidviewscrollerlab.R;

/**
 * View的滚动（动画滚动）
 */
public class Case2Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.case2_layout);

        bindEvent();
    }

    private void bindEvent() {
        final Case2ViewGroup containerViewGroup = (Case2ViewGroup) findViewById(R.id.container_viewgroup);
        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
        View.OnClickListener onButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = containerViewGroup.getCurrentIndex();
                int targetIndex = currentIndex;
                switch (v.getId()) {
                    case R.id.button_left:
                        targetIndex = currentIndex - 1;
                        break;
                    case R.id.button_right:
                        targetIndex = currentIndex + 1;
                        break;
                }
                containerViewGroup.moveToIndex(targetIndex);
            }
        };
        leftButton.setOnClickListener(onButtonClickListener);
        rightButton.setOnClickListener(onButtonClickListener);
    }


}
