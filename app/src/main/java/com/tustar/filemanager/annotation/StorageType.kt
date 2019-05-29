package com.tustar.filemanager.annotation

import androidx.annotation.IntDef

const val TYPE_IMAGE = 1
const val TYPE_AUDIO = 2
const val TYPE_VIDEO = 3
const val TYPE_DOC = 4
const val TYPE_APP = 5
const val TYPE_ARCHIVES = 6
const val TYPE_STORAGE_PHONE = 7
const val TYPE_STORAGE_SDCARD = 8
const val TYPE_STORAGE_USB = 9

@IntDef(TYPE_IMAGE,
        TYPE_AUDIO,
        TYPE_VIDEO,
        TYPE_DOC,
        TYPE_APP,
        TYPE_ARCHIVES,
        TYPE_STORAGE_PHONE,
        TYPE_STORAGE_SDCARD,
        TYPE_STORAGE_USB)

@Retention(AnnotationRetention.SOURCE)
annotation class StorageType

