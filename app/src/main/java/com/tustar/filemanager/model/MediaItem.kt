package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

open class MediaItem : DetailItem() {

    override var name: String? = null
    override var type: String? = null
    override val isHidden: Boolean
        get() = name?.startsWith(".") ?: false

    override var lastModified = 0L
    override var length: Long = 0L
    override var uri: Uri? = null
    override val isMediaFile = true

    var bucketId: Long? = null
    var bucketName: String? = null

    companion object {
        fun toList(cursor: Cursor): List<MediaItem> {
            val items = mutableListOf<MediaItem>()
            cursor.moveToFirst()
            do {
                val item = MediaItem()
                //
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.MediaColumns._ID))
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.MediaColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.MediaColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.MediaColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.MediaColumns.SIZE))
                //
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                item.uri = ContentUris.withAppendedId(
                        MediaStore.Files.getContentUri("external"), id)
                items += item
            } while (cursor.moveToNext())

            return items
        }
    }
}

