package com.sw.haruka.helper.binds

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sw.haruka.R
import com.sw.haruka.helper.utils.FileUtils
import com.sw.haruka.helper.utils.ImageUtil
import java.io.File

/**
 * Created by Swsbty on 2021/03/14
 */

@BindingAdapter(value = ["url"])
fun imgUrl(imageView: ImageView, url: String) {
    ImageUtil.load(url, imageView)
}


@BindingAdapter(value = ["visibleGone"])
fun visibleGone(v: View, visible: Boolean) {
    v.visibility = if (visible) View.VISIBLE else View.GONE
}


@BindingAdapter(value = ["filePath"])
fun filePath(v: ImageView, path: String) {
    if (File(path).isDirectory) {
        v.setImageResource(R.drawable.folder_icon)
        return
    }
    val type = FileUtils.getSuffix(path)
    if (FileUtils.isImg(type)) {
        ImageUtil.loadFile(path, v, -1)
    } else {
        v.setImageResource(FileUtils.getTypeImage(type))
    }
}