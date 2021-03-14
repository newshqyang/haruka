package com.sw.haruka.helper.extens

import android.app.Activity
import android.content.Intent
import android.os.Bundle

//////////////////////////////Activity///////////////////////////////
fun Activity.navigate2(clazz: Class<*>, bundle: Bundle? = null) {
    startActivity(Intent(this, clazz).apply {
        bundle?.let {
            putExtras(it)
        }
    })
}