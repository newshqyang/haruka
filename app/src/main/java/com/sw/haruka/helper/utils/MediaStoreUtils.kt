package com.sw.haruka.helper.utils

import android.media.MediaScannerConnection
import com.sw.haruka.App

/**
 * Created by Swsbty on 2021/03/14
 */
object MediaStoreUtils {

    /* 刷新媒体库 */
    fun refresh(paths: Array<String>) {
        MediaScannerConnection.scanFile(App.instance(), paths, null, null)
    }

}