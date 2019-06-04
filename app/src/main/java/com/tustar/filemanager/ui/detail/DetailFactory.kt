package com.tustar.filemanager.ui.detail

import com.tustar.filemanager.annotation.*

object DetailFactory {
    fun create(params: DetailParams): DetailFragment {
        return when (params.type) {
            TYPE_IMAGE -> ImageFragment.newInstance()
            TYPE_AUDIO -> AudioFragment.newInstance()
            TYPE_VIDEO -> VideoFragment.newInstance()
            TYPE_DOC -> DocFragment.newInstance()
            TYPE_APP -> AppFragment.newInstance()
            TYPE_ARCHIVES -> ArchivesFragment.newInstance()
            TYPE_STORAGE_PHONE, TYPE_STORAGE_SDCARD, TYPE_STORAGE_USB ->
                VolumeFragment.newInstance(params)
            else -> throw IllegalArgumentException("Unknown category type")
        }
    }
}