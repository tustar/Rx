package com.tustar.filemanager.model

import android.content.ContentUris
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore


class ImageFileItem : MediaFileItem() {

    var documentId: String? = null
    var originalDocumentId: String? = null
    var ownerPackageName: String? = null
    var relativePath: String? = null


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
            }
            return items
        }
    }
}

