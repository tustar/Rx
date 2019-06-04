package com.tustar.filemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import com.tustar.filemanager.model.AudioFileItem
import com.tustar.filemanager.model.ImageFileItem
import com.tustar.filemanager.model.VideoFileItem

class MediaStoreRepository(val context: Context) {

    private val resolver = context.applicationContext.contentResolver

    fun queryImageBucketCount(): Int? {
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
        val selection = "0=0) GROUP BY (${MediaStore.Images.ImageColumns.BUCKET_ID}"
        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    return cursor?.count
                }
    }

    fun queryImageBuckets(): List<ImageFileItem> {
        val projection = arrayOf(
                COLUMN_COUNT,
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_MODIFIED
        )
        val selection = "0=0) GROUP BY (${MediaStore.Images.ImageColumns.BUCKET_ID}"
        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return ImageFileItem.toBucketList(cursor)
                    }
                }
        return emptyList()
    }

    @SuppressLint("InlinedApi")
    fun queryImagesByBucketId(bucketId: Long): List<ImageFileItem> {
        val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.SIZE,
                    // Android Q
                    MediaStore.Images.ImageColumns.DOCUMENT_ID,
                    MediaStore.Images.ImageColumns.ORIGINAL_DOCUMENT_ID,
                    MediaStore.Images.ImageColumns.OWNER_PACKAGE_NAME,
                    MediaStore.Images.ImageColumns.RELATIVE_PATH
            )
        } else {
            arrayOf(
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.SIZE)
        }
        val selection = " ${MediaStore.Images.ImageColumns.BUCKET_ID}=$bucketId "
        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return ImageFileItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryAudioCount(): Int? {
        val projection = arrayOf("COUNT(${MediaStore.Audio.AudioColumns._ID})")
        resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryAudios(): List<AudioFileItem> {
        val projection = arrayOf(
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.MIME_TYPE,
                MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                MediaStore.Audio.AudioColumns.SIZE
        )
        val selection = "0=0"
        resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return AudioFileItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryVideoCount(): Int? {
        val projection = arrayOf("COUNT(${MediaStore.Video.VideoColumns._ID})")
        resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryVideos(): List<VideoFileItem> {
        val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.SIZE
        )
        val selection = "0=0"
        resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return VideoFileItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    companion object {

        private var INSTANCE: MediaStoreRepository? = null
        const val COLUMN_COUNT = "COUNT(${BaseColumns._ID}) as ${BaseColumns._COUNT}"

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