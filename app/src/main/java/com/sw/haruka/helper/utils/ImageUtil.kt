package com.sw.haruka.helper.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sw.haruka.App
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ImageUtil {

    fun loadFile(path: String, imageView: ImageView, maxWidth: Int = 150) {
        Glide.with(App.instance()).load(cut(path, maxWidth)).into(imageView)
    }

    fun load(url: String, imageView: ImageView) {
        Glide.with(App.instance()).load(url).into(imageView)
    }

    fun load(bm: Bitmap, imageView: ImageView) {
        Glide.with(App.instance()).load(bm).into(imageView)
    }


    fun getBitmap(path: String): Bitmap {
        val ops = BitmapFactory.Options()
        ops.inSampleSize = 200
        return BitmapFactory.decodeFile(path, ops)
    }


    fun cut(path: String) = cut(getBitmap(path))

    fun cut(path: String, maxWidth: Int) = cut(getBitmap(path), maxWidth)

    fun cut(bm: Bitmap): Bitmap {
        return cut(bm, 0)
    }

    fun cut(bm: Bitmap, maxWidth: Int): Bitmap {
        return bm
    }

    private fun compressImage(image: Bitmap): Bitmap {
        val baos = ByteArrayOutputStream()
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val options = 1
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().size / 1024 > 10) {
            //重置baos即清空baos
            baos.reset()
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        val isBm = ByteArrayInputStream(baos.toByteArray())
        //把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null)!!
    }
}