package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore


class VideoItem : MediaItem() {
    companion object {
        fun toList(cursor: Cursor): List<VideoItem> {
            val items = mutableListOf<VideoItem>()
            if (cursor.moveToFirst() && cursor.count > 0) {
                do {
                    val item = VideoItem()
                    //
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
                    //
                    item.name = displayName
                    item.type = mimeType
                    item.lastModified = dateModified
                    item.length = size
                    item.uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    items += item
                } while (cursor.moveToNext())
            }
            return items
        }
    }
}

