package com.tustar.rxjava.ui.transfer

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.tustar.rxjava.util.FileType
import com.tustar.rxjava.util.FileUtils
import java.io.File


class FileModel {
    var path: String? = null
    var version: String? = null
    var size: Long = 0L
    var name: String? = null
    var packageName: String? = null
    var fileType: Int = 0
    var isInstalled: Boolean = false
    var icon: Drawable? = null

    companion object {
        fun toModel(context: Context, file: File): FileModel {
            val fileModel = FileModel()
            val path = file.absolutePath
            if (file.extension.endsWith("apk", ignoreCase = true)) {
                val pm = context.packageManager
                val packageInfo = pm.getPackageArchiveInfo(file.absolutePath, 0)
                val appInfo = packageInfo.applicationInfo
                appInfo.sourceDir = path
                appInfo.publicSourceDir = path
                val packageName = appInfo.packageName
                var icon = pm.getApplicationIcon(appInfo)
                var appName = pm.getApplicationLabel(appInfo).toString()
                if (TextUtils.isEmpty(appName)) {
                    appName = FileUtils.getApplicationName(context, packageName)
                }
                if (icon == null) {
                    icon = FileUtils.getIconByPackageName(context, packageName)
                }
                fileModel.name = appName
                fileModel.packageName = packageName
                fileModel.path = path
                fileModel.size = file.length()
                fileModel.version = packageInfo.versionName
                fileModel.icon = icon
                fileModel.fileType = FileType.TYPE_APK
                fileModel.isInstalled = FileUtils.isInstalled(context, packageName)
            } else {
                fileModel.fileType = FileUtils.getFileType(path)
                fileModel.path = path
                val pathItems = path.split(File.separator)
                fileModel.name = pathItems[pathItems.size - 1]
                fileModel.size = file.length()
                val icon = ContextCompat.getDrawable(context, FileUtils.getFileTypeIcon(path))
                fileModel.icon = icon
            }

            return fileModel
        }
    }
}