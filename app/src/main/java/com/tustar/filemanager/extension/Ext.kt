package com.tustar.filemanager.extension

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.storage.StorageVolume


fun <T> Iterable<T>.sql() = joinToString(",", "(", ")") {
    "'$it'"
}

fun StorageVolume.uid(): String? =
        if (isPrimary) {
            "primary"
        } else {
            uuid
        }

@SuppressLint("DiscouragedPrivateApi")
fun StorageVolume.getPath(): String? =
        try {
            this.javaClass.getDeclaredMethod("getPath")
                    .invoke(this) as String?
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


fun Cursor.getCursorString(columnName: String): String? {
    val index = getColumnIndex(columnName)
    return if (index != -1) getString(index) else null
}

/**
 * Missing or null values are returned as -1.
 */
fun Cursor.getCursorLong(columnName: String): Long {
    val index = getColumnIndex(columnName)
    if (index == -1) return -1
    val value = getString(index) ?: return -1
    try {
        return java.lang.Long.parseLong(value)
    } catch (e: NumberFormatException) {
        return -1
    }

}

/**
 * Missing or null values are returned as 0.
 */
fun Cursor.getCursorInt(columnName: String): Int {
    val index = getColumnIndex(columnName)
    return if (index != -1) getInt(index) else 0
}