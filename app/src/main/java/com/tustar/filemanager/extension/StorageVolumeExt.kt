package com.tustar.filemanager.extension

import android.os.storage.StorageVolume

fun StorageVolume.uid(): String? =
        if (isPrimary) {
            "primary"
        } else {
            uuid
        }