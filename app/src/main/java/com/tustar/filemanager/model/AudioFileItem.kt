package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class AudioFileItem : MediaFileItem() {
    companion object {
        fun toList(cursor: Cursor): List<AudioFileItem> {
            val items = mutableListOf<AudioFileItem>()
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Audio.AudioColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Audio.AudioColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Audio.AudioColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Audio.AudioColumns.SIZE))
                val item = AudioFileItem()
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                item.uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                items += item
            }
            return items
        }
    }
}

