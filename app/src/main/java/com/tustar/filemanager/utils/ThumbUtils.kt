package com.tustar.filemanager.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import com.tustar.rxjava.util.Logger
import java.io.File
import java.io.IOException

object ThumbUtils {

    @JvmStatic
    fun computeSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels)
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    @JvmStatic
    private fun computeInitialSampleSize(options: BitmapFactory.Options,
                                         minSideLength: Int,
                                         maxNumOfPixels: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound = if (maxNumOfPixels == -1) 1 else Math.ceil(Math.sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1)
            128
        else
            Math.min(Math.floor(w / minSideLength),
                    Math.floor(h / minSideLength)).toInt()
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound
        }
        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    @JvmStatic
    fun computeReduceSize(width: Int, height: Int, maxLen: Int): Float {
        var result = -1f
        val srcLen = if (width > height) width else height

        if (srcLen > maxLen) {
            result = maxLen.toFloat() / srcLen.toFloat()
        }

        return result
    }

    @JvmStatic
    fun zoomInOut(bitmap: Bitmap, fW: Float, fH: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(fW, fH) // 长和宽放大缩小的比例
        val resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        return resizeBmp
    }

    @JvmStatic
    fun getDrawableForImage(context: Context?, filePath: String?): Drawable? {
        if (context == null || filePath == null) {
            return null
        }

        val file = File(filePath)
        if (!file.exists()) {
            return null
        }

        var currentIcon: Drawable? = null

        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888

        val where = "(_data) = '" + SqlUtils.sqlEscapeString(filePath) + "'"
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID), where, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                val b = MediaStore.Images.Thumbnails.getThumbnail(context.contentResolver, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, opt)

                currentIcon = zoomDrawable(context, filePath, b)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        if (currentIcon == null) {
            opt.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, opt)
            opt.inJustDecodeBounds = false

            opt.inSampleSize = computeSampleSize(opt, -1,
                    Constants.MAX_WIDTH * Constants.MAX_WIDTH)
            val b = BitmapFactory.decodeFile(filePath, opt)

            currentIcon = zoomDrawable(context, filePath, b)
        }
        return currentIcon
    }

    @JvmStatic
    fun zoomDrawable(context: Context, filePath: String, b: Bitmap?): Drawable? {
        var b = b
        var currentIcon: Drawable? = null
        if (b != null) {
            val f = computeReduceSize(b.width, b.height, Constants.MAX_WIDTH)
            if (f > 0) {
                b = zoomInOut(b, f, f)
            }
            b = getPortBitmap(filePath, b)
            currentIcon = BitmapDrawable(context.resources, b)
        }
        return currentIcon
    }

    @JvmStatic
    fun getDrawableForImageByScreenWidth(context: Context?, filePath: String?): Drawable? {
        if (context == null || filePath == null) {
            return null
        }

        val file = File(filePath)
        if (!file.exists()) {
            return null
        }

        var currentIcon: Drawable? = null

        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888

        val where = "(_data) = '" + SqlUtils.sqlEscapeString(filePath) + "'"
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID), where, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                val b = MediaStore.Images.Thumbnails.getThumbnail(context.contentResolver, id,
                        MediaStore.Images.Thumbnails.MINI_KIND, opt)

                currentIcon = zoomDrawableByScreenWidth(context, filePath, b)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        if (currentIcon == null) {
            opt.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, opt)
            opt.inJustDecodeBounds = false

            opt.inSampleSize = computeSampleSize(opt, -1,
                    Constants.MAX_WIDTH * Constants.MAX_WIDTH)
            val b = BitmapFactory.decodeFile(filePath, opt)

            currentIcon = zoomDrawableByScreenWidth(context, filePath, b)
        }
        return currentIcon
    }

    @JvmStatic
    private fun zoomDrawableByScreenWidth(context: Context, filePath: String, b: Bitmap?): Drawable? {
        var b = b
        var currentIcon: Drawable? = null
        if (b != null) {
            b = getPortBitmap(filePath, b)
            val widthPixels = context.resources.displayMetrics.widthPixels
            val f = widthPixels.toFloat() / b.width.toFloat()
            if (f > 0) {
                b = zoomInOut(b, f, f)
            }
            currentIcon = BitmapDrawable(context.resources, b)
        }
        return currentIcon
    }

    @JvmStatic
    fun getDrawableForAudio(context: Context, filePath: String): Drawable? {
        var currentIcon: Drawable? = null
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(filePath)
            val albumArt = retriever.embeddedPicture
            if (albumArt != null) {
                val opt = BitmapFactory.Options()
                opt.inPreferredConfig = Bitmap.Config.ARGB_8888
                opt.inJustDecodeBounds = true
                BitmapFactory.decodeByteArray(albumArt, 0, albumArt.size, opt)
                opt.inJustDecodeBounds = false
                opt.inSampleSize = computeSampleSize(opt, -1,
                        Constants.MAX_WIDTH * Constants.MAX_WIDTH)

                var b: Bitmap? = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.size, opt)
                if (b != null) {
                    val f = computeReduceSize(b.width, b.height, Constants.MAX_WIDTH)
                    if (f > 0) {
                        b = zoomInOut(b, f, f)
                    }
                    currentIcon = BitmapDrawable(context.resources, b)
                }
            }
            retriever.release()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return currentIcon
    }

    @JvmStatic
    fun getDrawableForVideo(context: Context, videoPath: String, width: Int, height: Int): Drawable? {
        var currentIcon: Drawable? = null

        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888

        val where = "(_data) = '" + SqlUtils.sqlEscapeString(videoPath) + "'"
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Video.Media._ID), where, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                var b: Bitmap? = MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, id,
                        MediaStore.Video.Thumbnails.MINI_KIND, opt)

                if (b != null) {
                    val f = computeReduceSize(b.width, b.height, Constants.MAX_WIDTH)
                    if (f > 0) {
                        b = zoomInOut(b, f, f)
                    }
                    currentIcon = BitmapDrawable(context.resources, b)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        if (currentIcon == null) {
            try {
                var bitmap: Bitmap? = null
                bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 96)
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
                if (bitmap != null) {
                    currentIcon = BitmapDrawable(context.resources, bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return currentIcon
    }

    @JvmStatic
    fun getDrawableForApk(context: Context, filePath: String): Drawable? {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES)
        if (info != null) {
            val appInfo = info.applicationInfo
            appInfo.sourceDir = filePath
            appInfo.publicSourceDir = filePath
            return pm.getDrawable(info.packageName, appInfo.icon, appInfo)            // try {
        }
        return null
    }

    @Synchronized
    fun getDrawableForPackageName(context: Context, packageName: String): Drawable? {
        val pm = context.packageManager
        try {
            val pi = pm.getPackageInfo(packageName, 0)
            val otherAppCtx = context.createPackageContext(packageName, Context
                    .CONTEXT_IGNORE_SECURITY)
            val displayMetrics = ArrayList<Int>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                displayMetrics.add(DisplayMetrics.DENSITY_XXXHIGH)
            }
            displayMetrics.add(DisplayMetrics.DENSITY_XXHIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_XHIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_HIGH)
            displayMetrics.add(DisplayMetrics.DENSITY_TV)
            for (displayMetric in displayMetrics) {
                try {
                    val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        otherAppCtx.resources.getDrawableForDensity(pi
                                .applicationInfo.icon, displayMetric, null)
                    } else {
                        otherAppCtx.resources.getDrawableForDensity(pi.applicationInfo.icon,
                                displayMetric)
                    }
                    if (drawable != null) {
                        return drawable
                    }
                } catch (e: Resources.NotFoundException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var appInfo = try {
            pm.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }

        return appInfo?.loadIcon(pm)
    }

    @JvmStatic
    fun rotateIfNeed(bitmap: Bitmap, orientation: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        return if (orientation != 0) {
            val temp = width
            width = height
            height = temp
            val mt = Matrix()
            mt.setRotate(orientation.toFloat(),
                    (width / 2).toFloat(), (height / 2).toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width,
                    bitmap.height, mt, true)
        } else {
            bitmap
        }
    }

    @JvmStatic
    fun getExifOrientation(filepath: String): Int {
        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(filepath)
        } catch (ex: IOException) {
            Logger.e(ex.message)
        }

        if (exif != null) {
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation != -1) {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            }
        }

        return degree
    }

    @JvmStatic
    fun getPortBitmap(filePath: String, bitmap: Bitmap): Bitmap {
        var bitmap = bitmap
        val orientation = getExifOrientation(filePath)
        if (orientation != 0) {
            bitmap = rotateIfNeed(bitmap, orientation)
        }
        return bitmap
    }
}
