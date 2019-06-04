package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import com.tustar.rxjava.util.Logger


class ImageItem : MediaItem() {

    var documentId: String? = null
    var originalDocumentId: String? = null
    var ownerPackageName: String? = null
    var relativePath: String? = null
    var isBucket: Boolean = false

    companion object {
        fun toBucketList(cursor: Cursor): List<ImageItem> {
            val items = mutableListOf<ImageItem>()
            cursor.moveToFirst()
            do {
                val item = ImageItem()
                //
                val count = cursor.getLong(cursor.getColumnIndex(
                        BaseColumns._COUNT))
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns._ID))
                val bucketId = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.BUCKET_ID))
                val bucketDisplayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DATE_MODIFIED))
                //
                item.bucketId = bucketId
                item.bucketName = bucketDisplayName
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = count
                item.uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                item.isBucket = true

                items += item
            } while (cursor.moveToNext())
            return items
        }

        fun toList(cursor: Cursor): List<ImageItem> {
            val items = mutableListOf<ImageItem>()
            cursor.moveToFirst()
            do {
                val item = ImageItem()
                //
                val id = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns._ID))
                Logger.d("id:$id")
                val displayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.SIZE))
                //
                item.name = displayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                item.uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                item.isBucket = false

                // Android Q
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val documentId = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.DOCUMENT_ID))
                    val originalDocumentId = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.ORIGINAL_DOCUMENT_ID))
                    val ownerPackageName = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.OWNER_PACKAGE_NAME))
                    val relativePath = cursor.getString(cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns.RELATIVE_PATH))
                    item.documentId = documentId
                    item.originalDocumentId = originalDocumentId
                    item.ownerPackageName = ownerPackageName
                    item.relativePath = relativePath
                }

                items += item
            } while (cursor.moveToNext())
            return items
        }
    }
}

