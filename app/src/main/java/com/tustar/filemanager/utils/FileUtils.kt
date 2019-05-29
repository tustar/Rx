package com.tustar.filemanager.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.rxjava.R
import org.jetbrains.anko.longToast
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

    fun openDocument(context: Context?, document: DetailFileItem) {
        context ?: return
        try {
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                data = document.uri
            }
            context.startActivity(openIntent)
        } catch (ex: ActivityNotFoundException) {
            context.longToast(context.resources.getString(R.string.error_no_activity, document.name))
        }
    }

    fun openDocument(context: Context?, uri: Uri) {
        context ?: return
        try {
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                data = uri
            }
            context.startActivity(openIntent)
        } catch (ex: ActivityNotFoundException) {
            context.longToast(context.resources.getString(R.string.error_no_activity, uri))
        }
    }
}