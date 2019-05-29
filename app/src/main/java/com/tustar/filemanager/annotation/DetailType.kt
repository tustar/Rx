package com.tustar.filemanager.annotation

import androidx.annotation.IntDef

const val TYPE_IMAGE = 1
const val TYPE_IMAGE_BUCKET = 2
const val TYPE_AUDIO = 3
const val TYPE_VIDEO = 4
const val TYPE_DOC = 5
const val TYPE_APP = 6
const val TYPE_ARCHIVES = 7
const val TYPE_STORAGE_PHONE = 8
const val TYPE_STORAGE_SDCARD = 9
const val TYPE_STORAGE_USB = 10


@IntDef(TYPE_IMAGE,
        TYPE_IMAGE_BUCKET,
        TYPE_AUDIO,
        TYPE_VIDEO,
        TYPE_DOC,
        TYPE_APP,
        TYPE_ARCHIVES,
        TYPE_STORAGE_PHONE,
        TYPE_STORAGE_SDCARD,
        TYPE_STORAGE_USB)

@Retention(AnnotationRetention.SOURCE)
annotation class DetailType

