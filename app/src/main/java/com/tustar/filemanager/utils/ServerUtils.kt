package com.tustar.filemanager.utils

object ServerUtils {

    private const val TEXT_CONTENT_TYPE: String = "text/html;charset=utf-8"
    private const val CSS_CONTENT_TYPE = "text/css;charset=utf-8"
    private const val BINARY_CONTENT_TYPE = "application/octet-stream"
    private const val JS_CONTENT_TYPE = "application/javascript"
    private const val PNG_CONTENT_TYPE = "application/x-png"
    private const val JPG_CONTENT_TYPE = "application/jpeg"
    private const val SWF_CONTENT_TYPE = "application/x-shockwave-flash"
    private const val WOFF_CONTENT_TYPE = "application/x-font-woff"
    private const val TTF_CONTENT_TYPE = "application/x-font-truetype"
    private const val SVG_CONTENT_TYPE = "image/svg+xml"
    private const val EOT_CONTENT_TYPE = "image/vnd.ms-fontobject"
    private const val MP3_CONTENT_TYPE = "audio/mp3"
    private const val MP4_CONTENT_TYPE = "video/mpeg4"

    fun getContentTypeByResourceName(resourceName: String): String {
        if (resourceName.endsWith(".css")) {
            return CSS_CONTENT_TYPE
        } else if (resourceName.endsWith(".js")) {
            return JS_CONTENT_TYPE
        } else if (resourceName.endsWith(".swf")) {
            return SWF_CONTENT_TYPE
        } else if (resourceName.endsWith(".png")) {
            return PNG_CONTENT_TYPE
        } else if (resourceName.endsWith(".jpg") || resourceName.endsWith(".jpeg")) {
            return JPG_CONTENT_TYPE
        } else if (resourceName.endsWith(".woff")) {
            return WOFF_CONTENT_TYPE
        } else if (resourceName.endsWith(".ttf")) {
            return TTF_CONTENT_TYPE
        } else if (resourceName.endsWith(".svg")) {
            return SVG_CONTENT_TYPE
        } else if (resourceName.endsWith(".eot")) {
            return EOT_CONTENT_TYPE
        } else if (resourceName.endsWith(".mp3")) {
            return MP3_CONTENT_TYPE
        } else if (resourceName.endsWith(".mp4")) {
            return MP4_CONTENT_TYPE
        }
        return ""
    }
}