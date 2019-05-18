package com.tustar.rxjava.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import com.tustar.rxjava.R
import java.io.File
import java.text.DecimalFormat

object FileUtils {
    fun getFileSize(length: Long): String {
        val df = DecimalFormat("######0.0")
        return when {
            length < 1024f -> length.toInt().toString() + "B"
            length < 1024 * 1024f -> df.format(length / 1024f) + "K"
            length < 1024f * 1024f * 1024f -> df.format(length.toFloat() / 1024f / 1024f) + "M"
            else -> df.format(length.toFloat() / 1024f / 1024f / 1024f) + "G"
        }
    }

    @Synchronized
    fun getIconByPackageName(context: Context, packageName: String): Drawable? {
        val pm = context.packageManager
        try {
            val pi = pm.getPackageInfo(packageName, 0)
            val otherAppCtx = context.createPackageContext(packageName, Context
                    .CONTEXT_IGNORE_SECURITY)
            val displayMetrics = ArrayList<Int>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                displayMetrics.add(DisplayMetrics.DENSITY_XXXHIGH)
            }
            displayMetrics.add(DisplayMetrics.DENSITY_XXHIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_XHIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_HIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_TV)
            for (displayMetric in displayMetrics) {
                try {
                    val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        otherAppCtx.resources.getDrawableForDensity(pi
                                .applicationInfo.icon, displayMetric, null)
                    } else {
                        otherAppCtx.resources.getDrawableForDensity(pi.applicationInfo.icon,
                                displayMetric)
                    }
                    if (drawable != null) {
                        return drawable
                    }
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var appInfo = try {
            pm.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }

        return appInfo?.loadIcon(pm)
    }

    fun isInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        val packageInfos = packageManager.getInstalledPackages(0)
        for (i in packageInfos.indices) {
            if (packageInfos[i].packageName
                            .equals(packageName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    fun getApplicationName(context: Context, packageName: String): String {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo?
        try {
            packageManager = context.applicationContext.packageManager
            applicationInfo = packageManager!!.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }

        return if (applicationInfo != null) {
            packageManager!!.getApplicationLabel(applicationInfo) as String
        } else {
            packageName
        }
    }

    fun getFileType(filePath: String): Int {
        val file = File(filePath)
        if (file.isDirectory) {
            return FileType.TYPE_FOLDER
        }

        val fileName = file.name

        FileType.FileTypes.forEachIndexed { index, types ->
            types.forEach { type ->
                if (fileName.endsWith(type)) {
                    return FileType.TypeStart[index]
                }
            }
        }

        return FileType.TYPE_UNKNOWN
    }

    fun getFileTypeIcon(path: String): Int {
        val fileType = getFileType(path)
        return when (fileType) {
            FileType.TYPE_FOLDER -> R.drawable.file_folder
            FileType.TYPE_IMAGE -> R.drawable.file_jpeg
            FileType.TYPE_AUDIO -> R.drawable.file_avi
            FileType.TYPE_VIDEO -> R.drawable.file_mp4
            FileType.TYPE_WEB -> R.drawable.file_html
            FileType.TYPE_TEXT -> R.drawable.file_txt
            FileType.TYPE_EXCEL -> R.drawable.file_excel
            FileType.TYPE_WORD -> R.drawable.file_doc
            FileType.TYPE_PPT -> R.drawable.file_ppt
            FileType.TYPE_PDF -> R.drawable.file_pdf
            FileType.TYPE_PACKAGE -> R.drawable.file_zip
            else -> R.drawable.file_unknow
        }
    }
}