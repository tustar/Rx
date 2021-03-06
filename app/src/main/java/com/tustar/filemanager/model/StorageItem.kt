package com.tustar.filemanager.model

import android.content.Context
import android.os.Build
import android.os.StatFs
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import androidx.annotation.DrawableRes
import androidx.annotation.WorkerThread
import com.tustar.filemanager.extension.getPath
import com.tustar.filemanager.utils.StorageUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger


data class StorageItem(@DrawableRes val icon: Int,
                       val name: String,
                       /**
                        * 文件系统中可被应用程序使用的空闲空间, 不包括保留存储空间(不能被普通应用程序使用)
                        */
                       var availableBytes: Long = 0L,
                       /**
                        * 文件系统中可被应用程序使用的空闲空间,包括保留存储空间(不能被普通应用程序使用)
                        */
                       var freeBytes: Long = 0L,
                       var totalBytes: Long = 0L,
                       val volume: StorageVolume,
                       val isSd: Boolean = false,
                       val isUsb: Boolean = false
) {

    companion object {

        @WorkerThread
        fun getStorageItems(context: Context): List<StorageItem> {
            val items = mutableListOf<StorageItem>()
            val sm = context.getSystemService(Context.STORAGE_SERVICE)
                    as StorageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sm.storageVolumes.forEach { volume ->
                    Logger.d("uuid:${volume.uuid}")

                    var availableBytes = 0L
                    var totalBytes = 0L
                    var icon = R.drawable.ic_sd_storage
                    var isSd = false
                    var isUsb = false
                    if (volume.isPrimary) {
                        icon = R.drawable.ic_root_smartphone
                    } else {
                        volume.uuid?.let {
                            val volumeInfo = StorageUtils.findVolumeByUuid(sm, it)
                            volumeInfo?.let { volumeInfo ->
                                val diskInfo = StorageUtils.getDisk(volumeInfo)
                                diskInfo?.apply {
                                    when {
                                        StorageUtils.isSd(diskInfo) ->{
                                            icon = R.drawable.ic_sd_storage
                                            isSd = true
                                        }
                                        StorageUtils.isUsb(diskInfo) -> {
                                            icon = R.drawable.ic_usb_storage
                                            isUsb = true
                                        }
                                    }
                                }
                            }
                        }
                    }

                    try {
                        val path = volume.getPath()
                        val statFs = StatFs(path)
                        availableBytes = statFs.availableBytes
                        totalBytes = statFs.totalBytes
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val item = StorageItem(icon = icon,
                            name = volume.getDescription(context),
                            volume = volume,
                            availableBytes = availableBytes,
                            totalBytes = totalBytes,
                            isSd = isSd,
                            isUsb = isUsb)
                    items.add(item)
                }
            }

            return items
        }
    }
}
