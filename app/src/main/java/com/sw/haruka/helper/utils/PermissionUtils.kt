package com.sw.haruka.helper.utils

import android.app.Activity
import androidx.core.app.ActivityCompat

/**
 * Created by Swsbty on 2021/03/14
 */
object PermissionUtils {

    const val REQUEST_PERMISSION_CODE = 9527
    fun file(act: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
            ActivityCompat.requestPermissions(act, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            return false
        }
        return true
    }

}