package com.tustar.filemanager.utils

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import com.tustar.filemanager.model.DetailItem
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import org.jetbrains.anko.longToast


object FileUtils {

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

    fun openDocument(context: Context?, item: DetailItem) {
        context ?: return
        try {
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                data = item.uri
                type = getMimeType(item)
            }
            context.startActivity(openIntent)
        } catch (ex: ActivityNotFoundException) {
            context.longToast(context.resources.getString(R.string.no_open_activity, item.name))
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
            context.longToast(context.resources.getString(R.string.no_open_activity, uri))
        }
    }

    fun share(context: Context?, items: List<DetailItem>) {
        if (context == null) {
            return
        }

        if (items.isNullOrEmpty()) {
            return
        }

        var intent: Intent
        var mimeType = "*/*"
        if (items.size == 1) {
            val item = items[0]
            if (item.isDirectory) {
                context.longToast(R.string.share_no_support_folder)
                return
            }
            intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, item.uri)
            }
            mimeType = getMimeType(item)
        } else {
            intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            val uris = arrayListOf<Uri>()
            items.forEach {
                if (it.isDirectory) {
                    context.longToast(R.string.share_no_support_folder)
                    return
                }
                it.uri?.let { uri ->
                    uris.add(uri)
                }
                mimeType = getMimeType(it)
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }

        Logger.d("mimeType:$mimeType")
        intent.apply {
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            type = mimeType
            putExtra("supportMultipleTheme", true)
        }
        try {
            val shareIntent = Intent.createChooser(intent, context.getString(R.string.share))
            context.startActivity(shareIntent)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            context.longToast(R.string.no_share_activity)
        }
    }


    fun delete(context: Context?, items: List<DetailItem>, block: () -> Unit) {
        if (context == null) {
            return
        }

        if (items.isNullOrEmpty()) {
            return
        }

        var preDeleteResult = true
        items.forEachIndexed { index, item ->
            item.uri?.let {
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        && item.isMediaFile) {
                    MediaStore.getDocumentUri(context, it)
                } else {
                    it
                }
                val file = DocumentFile.fromTreeUri(context, uri)
                var itemResult = file?.delete() ?: false
                // 中途删除失败，刷新文件，并中止
                if (index > 0 && preDeleteResult && !itemResult) {
                    block()
                    return
                }
                // 合并删除结果
                preDeleteResult = preDeleteResult && itemResult
            }
        }

        block()
    }

    fun showDetail(context: Context?, items: List<DetailItem>) {
        if (context == null) {
            return
        }

        if (items.isNullOrEmpty() || items.size != 1) {
            return
        }

        val item = items[0]
        val build = AlertDialog.Builder(context)
                .setTitle(item.name)
                .setMessage("${context.getString(R.string.file_path)}${item.uri}\n" +
                        "${context.getString(R.string.file_type)}${item.getFileType()}\n" +
                        "${context.getString(R.string.file_lastModified)}${DateUtils.millisToUTCDate(item.lastModified)}")
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .setOnCancelListener {
                    it.dismiss()
                }
        build.create().show()

    }

    @JvmStatic
    fun getMimeType(item: DetailItem): String {
        val fileType = item.getFileType()
        return FileType.MIME_TYPES[fileType] ?: "*/*"
    }
}