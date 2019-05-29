package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class VideoFileItem : MediaFileItem() {
    companion object {
        fun toList(cursor: Cursor): List<VideoFileItem> {
            val items = mutableListOf<VideoFileItem>()
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Video.VideoColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Video.VideoColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Video.VideoColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Video.VideoColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Video.VideoColumns.SIZE))
                val item = VideoFileItem()
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                item.uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                items += item
            }
            return items
        }
    }
}

