package com.sw.haruka.view.explorer.viewmodel

import com.sw.haruka.helper.utils.FileUtils
import java.io.File
import java.io.FileInputStream

/**
 * Created by Swsbty on 2021/03/14
 */
class FileItemWrapper constructor(val file: File, val isSelecting:Boolean = false, val check: Boolean = false) {

    val name: String = file.name
    val path: String = file.path
    val hasArrow = file.isDirectory && !isSelecting
    fun date(): String {
        var d = FileUtils.getFileUpdateTime(file)
        if (file.isFile) {
            d += "  " + FileUtils.getFormatSize(FileInputStream(file).available().toLong())
        }
        return d
    }

}