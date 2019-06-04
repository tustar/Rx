package com.tustar.filemanager.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tustar.filemanager.model.DetailItem
import com.tustar.rxjava.R

object FileType {

    val IMAGE_4 = arrayListOf(".bmp", ".gif", ".jpg", ".png", ".dng")
    val IMAGE_5 = arrayListOf(".webp", ".jpeg", ".wbmp")

    val AUDIO_4 = arrayListOf(".3gp", ".aac", ".amr", ".mp3", ".ogg", ".wav", ".m4a", ".mid")
    val AUDIO_5 = arrayListOf(".flac")
    val AUDIOS = mutableListOf<String>().apply {
        addAll(AUDIO_4)
        addAll(AUDIO_5)
    }.map { it.substringAfterLast(".", "") }

    val VIDEO_3 = arrayListOf(".ts")
    val VIDEO_4 = arrayListOf(".3g2", ".3gp", ".avi", ".f4v", ".mkv", ".mov", ".mp4", ".mpg")
    val VIDEO_5 = arrayListOf(".rmvb", ".webm", ".3gpp")

    val DOC_4 = arrayListOf(".htm", ".pdf", ".txt", ".doc", ".ppt", ".xls")
    val DOC_5 = arrayListOf(".epub", ".mobi", ".html", ".docx", ".pptx", ".xlsx")

    val APP_4 = arrayListOf(".apk")

    val ARCHIVE_3 = arrayListOf(".7z")
    val ARCHIVE_4 = arrayListOf(".zip", ".rar")

    fun getDrawable(context: Context, item: DetailItem): Drawable? {
        if (item.isDirectory) {
            return ContextCompat.getDrawable(context, R.drawable.format_folder)
        }

        var resId = when (item.getFileType()) {
            "apk" -> R.drawable.format_app
            "pdf" -> R.drawable.format_pdf
            "html", "htm" -> R.drawable.format_html
            "excel", "xls", "xlsx" -> R.drawable.format_excel
            "ppt", "pptx" -> R.drawable.format_ppt
            "doc", "docx" -> R.drawable.format_word
            "7z", "zip", "rar" -> R.drawable.format_zip
            "txt" -> R.drawable.format_text
            "bt" -> R.drawable.format_torrent
            "ebook" -> R.drawable.format_ebook
            "chm" -> R.drawable.format_chm
            ".fl", ".flash" -> R.drawable.format_flash
            in AUDIOS -> R.drawable.format_audio
            else -> -1
        }

        return if (resId == -1) {
            null
        } else {
            ContextCompat.getDrawable(context, resId)
        }
    }
}