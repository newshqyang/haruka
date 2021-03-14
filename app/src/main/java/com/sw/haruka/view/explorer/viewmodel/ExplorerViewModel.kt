package com.sw.haruka.view.explorer.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import java.io.File

/**
 * Created by Swsbty on 2021/03/14
 */
class ExplorerViewModel : ViewModel() {

    val files = ObservableArrayList<FileItemWrapper>()

    fun getFiles(rootPath: String) {
        files.addAll(File(rootPath).listFiles().map {
            FileItemWrapper(it)
        })
    }
}