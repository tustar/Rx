package com.tustar.filemanager.repository

import android.content.Context
import android.provider.MediaStore
import com.tustar.filemanager.model.AudioFileItem
import com.tustar.filemanager.model.ImageBucketFileItem
import com.tustar.filemanager.model.ImageFileItem
import com.tustar.filemanager.model.VideoFileItem

class MediaStoreRepository(val context: Context) {

    private val resolver = context.applicationContext.contentResolver

    fun queryImageBucketCount(): Int? {
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                IMAGE_BUCKET_SELECTION,
                null,
                null)
                .use { cursor ->
                    return cursor?.count
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
        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                IMAGE_BUCKET_SELECTION,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return ImageBucketFileItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryImagesByeBucketId(bucketId: Long): List<ImageFileItem> {
        val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.SIZE
        )
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
                MediaStore.Video.VideoColumns._ID,
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
        private const val IMAGE_BUCKET_SELECTION = "0=0) GROUP BY (${MediaStore.Images.ImageColumns.BUCKET_ID}"
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