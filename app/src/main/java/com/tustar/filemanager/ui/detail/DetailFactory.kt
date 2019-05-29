package com.tustar.filemanager.ui.detail

import android.net.Uri
import com.tustar.filemanager.annotation.*

object DetailFactory {
    fun create(@StorageType storageType: Int, directoryUri: Uri? = null): DetailFragment {
        return when (storageType) {
            TYPE_IMAGE -> ImageFragment.newInstance()
            TYPE_AUDIO -> AudioFragment.newInstance()
            TYPE_VIDEO -> VideoFragment.newInstance()
            TYPE_DOC -> DocFragment.newInstance()
            TYPE_APP -> AppFragment.newInstance()
            TYPE_ARCHIVES -> ArchivesFragment.newInstance()
            TYPE_STORAGE_PHONE, TYPE_STORAGE_SDCARD, TYPE_STORAGE_USB -> {
                val uri = directoryUri
                        ?: throw IllegalArgumentException("Must pass URI of directory to open")
                VolumnFragment.newInstance(uri)
            }
            else -> DetailFragment.newInstance()
        }
    }
}