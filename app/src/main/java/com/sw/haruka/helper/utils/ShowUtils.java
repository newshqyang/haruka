package com.sw.haruka.helper.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.RelativeLayout;

public class ShowUtils {

    /**
     * 弹出视图，向上弹出
     * @param view  视图
     */
    public static void pop(final View view) {
        core(view, ObjectAnimator.ofInt(-1 * view.getHeight(), 0));
    }

    /**
     * 隐藏视图，向下退出
     * @param view  视图
     */
    public static void hide(View view) {
        core(view, ObjectAnimator.ofInt(0, -1 * view.getHeight()));
    }

    private static void core(final View view, ValueAnimator animator) {
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.bottomMargin = (int) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        animator.setDuration(200);
        animator.start();
    }
}
