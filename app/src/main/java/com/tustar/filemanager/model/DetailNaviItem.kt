package com.tustar.filemanager.model

import android.content.Context
import android.net.Uri
import com.tustar.rxjava.R

data class DetailNaviItem(val name: String,
                          val uri: Uri? = null,
                          val isHome: Boolean = false) {

    companion object {
        fun getNaviHome(context: Context) = DetailNaviItem(
                name = context.getString(R.string.navi_home),
                isHome = true
        )
    }
}