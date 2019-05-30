package com.tustar.filemanager.model

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import androidx.annotation.DrawableRes
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger

data class VolumeItem(@DrawableRes val icon: Int,
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
                      val volume: StorageVolume) {

    companion object {
        fun getVolumeItems(context: Context): List<VolumeItem> {
            val items = mutableListOf<VolumeItem>()
            val sm = context.getSystemService(Context.STORAGE_SERVICE)
                    as StorageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sm.storageVolumes.forEach { volume ->
                    var icon = if (volume.isPrimary) {
                        R.drawable.ic_root_smartphone
                    } else {
                        R.drawable.ic_sd_storage
                    }
                    val item = VolumeItem(icon = icon,
                            name = volume.getDescription(context),
                            volume = volume)
                    items.add(item)
                }
            }

            val usbManager = context.getSystemService(Context.USB_SERVICE)
                    as UsbManager
            var productName = ""
            usbManager.deviceList.values.forEach { usbDevice ->
                Logger.d("productName:${usbDevice.productName}")
                if (usbDevice.interfaceCount == 1) {
                    if (usbDevice.getInterface(0).interfaceClass == 8) {
                        productName = usbDevice.productName.toString()
                        if (productName.isEmpty()) {
                            "USB-OTG device detected : "
                        } else {
                            "USB-OTG device detected : $productName"
                        }
                    }
                } else {

                }
            }
            return items
        }
    }
}
