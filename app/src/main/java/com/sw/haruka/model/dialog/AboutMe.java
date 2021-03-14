package com.sw.haruka.model.dialog;

import android.content.Context;
import android.view.View;

import com.sw.haruka.R;

public class AboutMe extends FullScreenDialog implements View.OnClickListener {
    public AboutMe(Context context) {
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.about_me);
        findViewById(R.id.imageButton_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton_close) {
            dismiss();
        }
    }
}
