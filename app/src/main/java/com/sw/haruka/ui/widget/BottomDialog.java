package com.sw.haruka.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sw.haruka.R;



class BottomDialog extends Dialog {

    public BottomDialog(Context context) {
        super(context, R.style.EditDialog);
    }

    @Override
    public void show() {
        super.show();
        /*
          设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Window window = getWindow();
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_bottom);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
