package com.tustar.filemanager.model

import android.net.Uri

abstract class DetailFileItem {
    abstract val name: String?
    abstract val type: String?
    abstract val isDirectory: Boolean
    abstract val isHidden: Boolean?
    abstract val lastModified: Long
    abstract val length: Long
    abstract val uri: Uri
}