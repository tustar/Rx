package com.tustar.filemanager.model

import android.net.Uri

open class MediaFileItem : DetailFileItem() {

    override var name: String? = null
    override var type: String? = null
    override val isHidden: Boolean
        get() = name?.startsWith(".") ?: false

    override var lastModified = 0L
    override var length: Long = 0L
    override var uri: Uri? = null
}

