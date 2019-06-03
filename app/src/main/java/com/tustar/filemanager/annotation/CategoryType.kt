package com.tustar.filemanager.annotation

import androidx.annotation.IntDef

const val TYPE_IMAGE = 1
const val TYPE_AUDIO = TYPE_IMAGE + 1
const val TYPE_VIDEO = TYPE_AUDIO + 1
const val TYPE_DOC = TYPE_VIDEO + 1
const val TYPE_APP = TYPE_DOC + 1
const val TYPE_ARCHIVES = TYPE_APP + 1
const val TYPE_STORAGE_PHONE = TYPE_ARCHIVES + 1
const val TYPE_STORAGE_SDCARD = TYPE_STORAGE_PHONE + 1
const val TYPE_STORAGE_USB = TYPE_STORAGE_SDCARD + 1


@IntDef(
        TYPE_IMAGE,
        TYPE_AUDIO,
        TYPE_VIDEO,
        TYPE_DOC,
        TYPE_APP,
        TYPE_ARCHIVES,
        TYPE_STORAGE_PHONE,
        TYPE_STORAGE_SDCARD,
        TYPE_STORAGE_USB)

@Retention(AnnotationRetention.SOURCE)
annotation class CategoryType

