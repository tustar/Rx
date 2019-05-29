package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


class ImageFileItem : MediaFileItem() {
    companion object {
        fun toList(cursor: Cursor): List<ImageFileItem> {
            val items = mutableListOf<ImageFileItem>()
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.SIZE))
                val item = ImageFileItem()
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                item.uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                items += item
            }
            return items
        }
    }
}

