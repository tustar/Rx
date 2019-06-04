package com.tustar.filemanager.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import com.tustar.filemanager.extension.sql
import com.tustar.filemanager.model.AudioFileItem
import com.tustar.filemanager.model.ImageItem
import com.tustar.filemanager.model.MediaItem
import com.tustar.filemanager.model.VideoItem
import com.tustar.filemanager.utils.FileType.APP_4
import com.tustar.filemanager.utils.FileType.ARCHIVE_3
import com.tustar.filemanager.utils.FileType.ARCHIVE_4
import com.tustar.filemanager.utils.FileType.DOC_4
import com.tustar.filemanager.utils.FileType.DOC_5

class MediaStoreRepository(val context: Context) {

    private val resolver = context.applicationContext.contentResolver

    fun queryImageBucketCount(): Int? {
        val projection = arrayOf(BaseColumns._ID)
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

    fun queryImageBuckets(): List<ImageItem> {
        val projection = arrayOf(
                COLUMN_COUNT,
                BaseColumns._ID,
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
                        return ImageItem.toBucketList(cursor)
                    }
                }
        return emptyList()
    }

    @SuppressLint("InlinedApi")
    fun queryImagesByBucketId(bucketId: Long): List<ImageItem> {
        val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                    BaseColumns._ID,
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
                    BaseColumns._ID,
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
                        return ImageItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryAudioCount(): Int? {
        val projection = arrayOf("COUNT(${BaseColumns._ID})")
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
                BaseColumns._ID,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.MIME_TYPE,
                MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                MediaStore.Audio.AudioColumns.SIZE
        )
        resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
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
        val projection = arrayOf("COUNT(${BaseColumns._ID})")
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

    fun queryVideos(): List<VideoItem> {
        val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.SIZE
        )
        resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return VideoItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryDocCount(): Int? {
        val projection = arrayOf("COUNT(${BaseColumns._ID})")
        val selection = getDocWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryDocs(): List<MediaItem> {
        val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
        )
        val selection = getDocWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return MediaItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    fun queryAppCount(): Int? {
        val projection = arrayOf("COUNT(${BaseColumns._ID})")
        val selection = getAppWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryApps(): List<MediaItem> {
        val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
        )
        val selection = getAppWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return MediaItem.toList(cursor)
                    }
                }
        return emptyList()
    }

    //
    fun queryArchivesCount(): Int? {
        val projection = arrayOf("COUNT(${BaseColumns._ID})")
        val selection = getArchiveWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.moveToFirst()
                    return cursor?.getInt(0)
                }
    }

    fun queryArchives(): List<MediaItem> {
        val projection = arrayOf(
                BaseColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.SIZE
        )
        val selection = getArchiveWhere()
        resolver.query(MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                null)
                .use { cursor ->
                    cursor?.let {
                        return MediaItem.toList(cursor)
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

        fun getDocWhere(): String {
            return StringBuilder().run {
                append("(lower(substr(${MediaStore.MediaColumns.DISPLAY_NAME}, -4)) in ")
                append(DOC_4.sql())
                append(")")
                append(" or ")
                append("(lower(substr(${MediaStore.MediaColumns.DISPLAY_NAME}, -5)) in ")
                append(DOC_5.sql())
                append(")")
                toString()
            }
        }

        fun getAppWhere(): String {
            return StringBuilder().run {
                append("(lower(substr(${MediaStore.MediaColumns.DISPLAY_NAME}, -4)) in (")
                append(APP_4.sql())
                append("))")
                toString()
            }
        }

        fun getArchiveWhere(): String {
            return StringBuilder().run {
                append("(lower(substr(${MediaStore.MediaColumns.DISPLAY_NAME}, -3)) in ")
                append(ARCHIVE_3.sql())
                append(")")
                append(" or ")
                append("(lower(substr(${MediaStore.MediaColumns.DISPLAY_NAME}, -4)) in ")
                append(ARCHIVE_4.sql())
                append(")")
                toString()
            }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}