package com.tustar.filemanager.utils

import android.content.Context
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.provider.DocumentsContract
import com.tustar.filemanager.model.RootInfo
import java.io.File

typealias VolumeInfo = Any
typealias DiskInfo = Any

object StorageUtils {
    fun findVolumeByUuid(sm: StorageManager, fsUuid: String): VolumeInfo? {
        return try {
            sm.javaClass.getDeclaredMethod("findVolumeByUuid", String::class.java)
                    .invoke(sm, fsUuid)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPath(volumeInfo: VolumeInfo): String? =
            try {
                val file = volumeInfo.javaClass.getDeclaredMethod("getPath")
                        .invoke(volumeInfo) as File
                file.path
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

    fun getDisk(volumeInfo: VolumeInfo): DiskInfo? {
        return try {
            volumeInfo.javaClass.getDeclaredMethod("getDisk")
                    .invoke(volumeInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isUsb(diskInfo: DiskInfo): Boolean {
        return try {
            diskInfo.javaClass.getDeclaredMethod("isUsb")
                    .invoke(diskInfo) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun isSd(diskInfo: DiskInfo): Boolean {
        return try {
            diskInfo.javaClass.getDeclaredMethod("isSd")
                    .invoke(diskInfo) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun queryBytes(context: Context, volume: StorageVolume): RootInfo? {
        val uuid = volume.uuid ?: return null
        val volumeUri = DocumentsContract.buildRootUri(
                Constants.EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uuid)
        val projection = arrayOf(
                DocumentsContract.Root.COLUMN_AVAILABLE_BYTES,
                DocumentsContract.Root.COLUMN_CAPACITY_BYTES,
                DocumentsContract.Root.COLUMN_DOCUMENT_ID,
                DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.COLUMN_ICON,
                DocumentsContract.Root.COLUMN_MIME_TYPES,
                DocumentsContract.Root.COLUMN_ROOT_ID,
                DocumentsContract.Root.COLUMN_SUMMARY,
                DocumentsContract.Root.COLUMN_TITLE)
        context.contentResolver.query(volumeUri, projection, null,
                null, null)
                .use { cursor ->
                    cursor?.let {
                        return RootInfo.fromRootsCursor(it)
                    }
                }

        return null
    }
}

