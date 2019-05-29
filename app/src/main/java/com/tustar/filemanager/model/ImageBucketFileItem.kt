package com.tustar.filemanager.model

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore


data class ImageBucketFileItem(
        var bucketId: Long? = null) : DetailFileItem() {

    override var name: String? = null
    override var type: String? = null
    override val isDirectory = false
    override val isHidden: Boolean
        get() = name?.startsWith(".") ?: false

    override var lastModified = 0L
    override var length: Long = 0L
    override var uri: Uri? = null


    companion object {
        fun toList(cursor: Cursor): List<ImageBucketFileItem> {
            val items = mutableListOf<ImageBucketFileItem>()
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val bucketId = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.BUCKET_ID))
                val bucketDisplayName = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
                val mimeType = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.MIME_TYPE))
                val dateModified = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.DATE_MODIFIED))
                val size = cursor.getLong(cursor.getColumnIndex(
                        MediaStore.Images.ImageColumns.SIZE))
                val item = ImageBucketFileItem(bucketId)
                item.name = bucketDisplayName
                item.type = mimeType
                item.lastModified = dateModified
                item.length = size
                items += item
            }
            return items
        }
    }
}

