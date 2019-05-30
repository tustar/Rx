package com.tustar.filemanager.ui.detail

import android.net.Uri
import com.tustar.filemanager.annotation.*

object DetailFactory {
    fun create(@CategoryType detailType: Int,
               directoryUri: Uri? = null,
               bucketId: Long = 0): DetailFragment {
        return when (detailType) {
            TYPE_IMAGE_BUCKET -> ImageBucketFragment.newInstance()
            TYPE_IMAGE_BUCKET_ITEM -> ImageBucketItemFragment.newInstance(bucketId)
            TYPE_AUDIO -> AudioFragment.newInstance()
            TYPE_VIDEO -> VideoFragment.newInstance()
            TYPE_DOC -> DocFragment.newInstance()
            TYPE_APP -> AppFragment.newInstance()
            TYPE_ARCHIVES -> ArchivesFragment.newInstance()
            TYPE_STORAGE_PHONE, TYPE_STORAGE_SDCARD, TYPE_STORAGE_USB -> {
                val uri = directoryUri
                        ?: throw IllegalArgumentException("Must pass URI of directory to open")
                VolumeFragment.newInstance(uri)
            }
            else -> DetailFragment.newInstance()
        }
    }
}