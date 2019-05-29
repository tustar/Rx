package com.tustar.filemanager.repository

import android.content.Context
import android.provider.MediaStore
import com.tustar.filemanager.model.ImageBucketFileItem

class MediaStoreRepository(val context: Context) {

    fun queryImageBucketCount(): Int? {
        val projection = arrayOf("COUNT(${MediaStore.Images.ImageColumns._ID})")
        val selection = "0=0) GROUP BY (${MediaStore.Images.ImageColumns.BUCKET_ID}"
        context.applicationContext.contentResolver
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        null,
                        null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryImageBucketItems(): List<ImageBucketFileItem> {
        val projection = arrayOf(
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.SIZE
        )
        val selection = "0=0) GROUP BY (${MediaStore.Images.ImageColumns.BUCKET_ID}"
        context.applicationContext.contentResolver
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        selection,
                        null,
                        null)
                .use { cursor ->
                    cursor?.let {
                        return ImageBucketFileItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryAudioCount(): Int? {
        val projection = arrayOf("COUNT(${MediaStore.Audio.AudioColumns._ID})")
        context.applicationContext.contentResolver
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryVideoCount(): Int? {
        val projection = arrayOf("COUNT(${MediaStore.Video.VideoColumns._ID})")
        context.applicationContext.contentResolver
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    companion object {

        private var INSTANCE: MediaStoreRepository? = null

        @JvmStatic
        fun get(context: Context) =
                INSTANCE ?: synchronized(MediaStoreRepository::class.java) {
                    INSTANCE ?: MediaStoreRepository(context).also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}