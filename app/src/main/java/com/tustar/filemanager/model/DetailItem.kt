package com.tustar.filemanager.model

import android.net.Uri

open class DetailItem {
    open val name: String? = null
    open val type: String? = null
    open val isDirectory: Boolean = false
    open val isHidden: Boolean = false
    open val lastModified: Long = 0L
    open val length: Long = 0L
    open val uri: Uri? = null
    var isChecked: Boolean = false

    fun getFileType() =
            when {
                name != null -> name?.substringAfterLast('.', "")
                uri != null -> uri.toString().substringAfterLast('.', "")
                else -> null
            }
}