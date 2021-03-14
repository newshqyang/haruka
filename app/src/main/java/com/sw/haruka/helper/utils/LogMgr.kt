package com.sw.haruka.helper.utils

import android.util.Log

/**
 * Created by Swsbty on 2021/03/14
 */
object LogMgr {

    fun d(s: String) {
        val clsName = Exception().stackTrace[1].className
        Log.d(">>>Haruka Log/$clsName<<<", s)
    }


}