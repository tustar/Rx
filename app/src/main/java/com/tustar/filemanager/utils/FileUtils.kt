package com.tustar.filemanager.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.tustar.filemanager.model.CachingDocumentFile
import com.tustar.rxjava.R
import org.jetbrains.anko.longToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
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

    private fun getFileTypeByName(fileName: String): Int {
        FileType.FileTypes.forEachIndexed { index, types ->
            types.forEach { type ->
                if (fileName.endsWith(type)) {
                    return FileType.TypeStart[index]
                }
            }
        }

        return FileType.TYPE_UNKNOWN
    }

    @JvmStatic
    fun getFileType(filePath: String): Int {
        val file = File(filePath)
        if (file.isDirectory) {
            return FileType.TYPE_FOLDER
        }

        return getFileTypeByName(file.name)
    }

    @JvmStatic
    fun getFileType(file: DocumentFile): Int {
        if (file.isDirectory) {
            return FileType.TYPE_FOLDER
        }

        file.name?.let { return getFileTypeByName(it) }

        return FileType.TYPE_UNKNOWN
    }
    private fun fileType2ResId(fileType: Int) =
            when (fileType) {
                FileType.TYPE_FOLDER -> R.drawable.format_folder
                FileType.TYPE_IMAGE -> R.drawable.format_image
                FileType.TYPE_AUDIO -> R.drawable.format_audio
                FileType.TYPE_VIDEO -> R.drawable.format_video
                FileType.TYPE_WEB -> R.drawable.format_html
                FileType.TYPE_TEXT -> R.drawable.format_text
                FileType.TYPE_EXCEL -> R.drawable.format_excel
                FileType.TYPE_WORD -> R.drawable.format_doc
                FileType.TYPE_PPT -> R.drawable.format_ppt
                FileType.TYPE_PDF -> R.drawable.format_pdf
                FileType.TYPE_PACKAGE -> R.drawable.format_archives
                else -> R.drawable.format_unkown
            }

    @JvmStatic
    fun getFileTypeDrawable(context: Context, path: String): Drawable? {
        val fileType = getFileType(path)
        val resId = fileType2ResId(fileType)
        return context.getDrawable(resId)
    }

    @JvmStatic
    fun getFileTypeDrawable(context: Context, file:DocumentFile): Drawable? {
        val fileType = getFileType(file)
        val resId = fileType2ResId(fileType)
        return context.getDrawable(resId)
    }

    @JvmStatic
    fun getFileTypeDrawableByName(context: Context, fileName:String): Drawable? {
        val fileType = getFileTypeByName(fileName)
        val resId = fileType2ResId(fileType)
        return context.getDrawable(resId)
    }

    private fun fileType2MimeType(fileType: Int): String {
        return when (fileType) {
            FileType.TYPE_IMAGE -> "image/*"
            FileType.TYPE_AUDIO -> "audio/*"
            FileType.TYPE_VIDEO -> "video/*"
            FileType.TYPE_WEB, FileType.TYPE_TEXT -> "text/*"
            FileType.TYPE_EXCEL,
            FileType.TYPE_WORD,
            FileType.TYPE_PPT,
            FileType.TYPE_PDF,
            FileType.TYPE_PACKAGE,
            FileType.TYPE_APK -> "application/*"
            else -> "*/*"
        }
    }

    @JvmStatic
    fun getShareType(path: String): String {
        val fileType = getFileType(path)
        return fileType2MimeType(fileType)
    }

    @Throws(IOException::class)
    fun copyFile(inputStream: InputStream, target: String) {
        inputStream.use {
            FileOutputStream(target).use { outputStream ->
                it.copyTo(outputStream, 2048)
            }
        }
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null,
                    null)!!.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }

        return result
    }

    @JvmStatic
    fun getFileUri(context: Context, filePath: String): Uri {
        val file = File(filePath)
        return FileProvider.getUriForFile(context, context.packageName + "" +
                ".fileprovider", file)
    }

    @JvmStatic
    fun share(context: Context, filePath: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUri(context, filePath))
        shareIntent.type = getShareType(filePath)
        context.startActivity(Intent.createChooser(shareIntent, ""))
    }

    @JvmStatic
    fun openFile(context: Context, filePath: String) {
        val fileType = getFileType(filePath)
        val file = File(filePath)

        if (file.isFile) {
            val uri = getFileUri(context, filePath)
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra("supportMultipleTheme", true);

                when (fileType) {
                    FileType.TYPE_IMAGE -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "image/*")
                    }
                    FileType.TYPE_AUDIO -> {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra("oneshot", 0)
                        putExtra("configchange", 0)
                        setDataAndType(uri, "audio/*")
                    }
                    FileType.TYPE_VIDEO -> {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra("oneshot", 0)
                        putExtra("configchange", 0)
                        setDataAndType(uri, "video/*")
                    }
                    FileType.TYPE_WEB -> {
                        setDataAndType(uri, "text/html")
                    }
                    FileType.TYPE_TEXT -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "text/plain")
                    }
                    FileType.TYPE_EXCEL -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "application/vnd.ms-excel")
                    }
                    FileType.TYPE_WORD -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "application/msword")
                    }
                    FileType.TYPE_PPT -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "application/vnd.ms-powerpoint")
                    }
                    FileType.TYPE_PDF -> {
                        addCategory("android.intent.category.DEFAULT")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        setDataAndType(uri, "application/pdf")
                    }
                    FileType.TYPE_PACKAGE,
                    FileType.TYPE_APK -> {
                        //兼容7.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            val contentUri = FileProvider.getUriForFile(context, context.packageName + "" +
                                    ".fileprovider", file)
                            setDataAndType(contentUri, "application/vnd.android.package-archive")
                        } else {
                            setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }
                    else -> {
                        addCategory("android.intent.category.DEFAULT")
                        setDataAndType(uri, "*/*")
                    }
                }
            }
            //
            context.startActivity(intent)
        }
    }

    fun openDocument(context: Context?, document: CachingDocumentFile) {
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
}