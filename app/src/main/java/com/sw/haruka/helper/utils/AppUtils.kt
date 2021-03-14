package com.sw.haruka.helper.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Created by Swsbty on 2021/03/14
 */
object AppUtils {

    /**
     * 进入全屏
     * @param v     当前Activity内任意的View
     */
    fun enterFullScreen(v: View) {
        val controller = ViewCompat.getWindowInsetsController(v)
        controller?.let {
            it.hide(WindowInsetsCompat.Type.statusBars())
            it.hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

    /**
     * 退出全屏
     * @param v     当前Activity内任意的View
     */
    fun exitFullScreen(v: View) {
        val controller = ViewCompat.getWindowInsetsController(v)
        controller?.show(WindowInsetsCompat.Type.statusBars())
        controller?.show(WindowInsetsCompat.Type.navigationBars())
    }

    fun whiteStatus(v: View) {
        val controller = ViewCompat.getWindowInsetsController(v)
        controller?.let {
            it.isAppearanceLightStatusBars = true
        }
    }

}