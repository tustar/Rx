package com.tustar.rxjava.ui.files

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.tustar.rxjava.util.FileType
import com.tustar.rxjava.util.FileUtils
import com.tustar.rxjava.util.ThumbUtils
import java.io.File
import java.io.FilenameFilter
import java.util.*


class FileModel {
    var icon: Drawable? = null
    var name: String? = null
    var fileCount: Int = 0
        get() {
            val showHidden = false
            val file = File(path)
            return if (file.isDirectory) {
                file.listFiles(object : FilenameFilter {
                    override fun accept(dir: File, filename: String): Boolean {
                        return showHidden || !filename.startsWith(".")

                    }
                }).size
            } else {
                0
            }
        }
    var path: String? = null
    var size: Long = 0L
    var lastModified: Long = Date().time
    var fileType: Int = 0
    // For apk
    var version: String? = null
    var packageName: String? = null
    var isInstalled: Boolean = false
    // For folder
    var isDirectory: Boolean = false

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
                    icon = ThumbUtils.getDrawableForPackageName(context, packageName)
                }
                fileModel.icon = icon
                fileModel.name = appName
                fileModel.path = path
                fileModel.size = file.length()
                fileModel.lastModified = file.lastModified()
                fileModel.fileType = FileType.TYPE_APK
                //
                fileModel.version = packageInfo.versionName
                fileModel.packageName = packageName
                fileModel.isInstalled = FileUtils.isInstalled(context, packageName)
                //
                fileModel.isDirectory = file.isDirectory
            } else {
                val icon = ThumbUtils.getDrawableForFile(context, path)
                fileModel.icon = icon
                val pathItems = path.split(File.separator)
                fileModel.name = pathItems[pathItems.size - 1]
                fileModel.path = path
                fileModel.size = file.length()
                fileModel.lastModified = file.lastModified()
                fileModel.fileType = FileUtils.getFileType(path)
                //
                fileModel.isDirectory = file.isDirectory
            }

            return fileModel
        }
    }
}