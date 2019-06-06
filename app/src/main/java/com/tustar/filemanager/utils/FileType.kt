package com.tustar.filemanager.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tustar.filemanager.model.DetailItem
import com.tustar.rxjava.R


object FileType {

    val MIME_TYPES = mapOf(
            "htm" to "text/html",
            "html" to "text/html",
            "epub" to "application/epub+zip",
            "mobi" to "application/x-mobipocket-ebook",
            "pdf" to "application/pdf",
            "txt" to "text/plain",
            "doc" to "application/msword",
            "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "ppt" to "application/vnd.ms-powerpoint",
            "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "xls" to "application/vnd.ms-excel",
            "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",

            "rss" to "application/rss+xml",
            "pub" to "application/x-mspublisher",
            "csv" to "text/csv",
            // apk
            "apk" to "application/vnd.android.package-archive",
            // pic
            "jpg" to "image/jpeg",
            "jpeg" to "image/jpeg",
            "png" to "image/png",
            "gif" to "image/gif",
            "bmp" to "image/bmp",
            "webp" to "image/webp",
            "dng" to "image/x-adobe-dng",

            "tif" to "image/tiff",
            "tiff" to "image/tiff",
            "ico" to "image/x-icon",
            "wbmp" to "image/vnd.wap.wbmp",

            // audio
//             "3gp" to "audio/*",
            "aac" to "audio/x-aac",
            "mp3" to "audio/mpeg",
            "ogg" to "audio/ogg",
            "wav" to "audio/x-wav",
            "flac" to "audio/flac",
            "m4a" to "audio/mp4a-latm",
            "mid" to "audio/midi",

            "mxmf" to "audio/mobile-xmf",
            "amr" to "audio/amr",
            "wma" to "audio/x-ms-wma",
            "mpg" to "audio/mpeg",
            "imy" to "audio/x-imelody",
            "midi" to "audio/midi",
            "ra" to "audio/x-pn-realaudio",
            "ram" to "audio/x-pn-realaudio",
            "weba" to "audio/webm",
            "mka" to "audio/x-matroska",

            // video
            "3g2" to "video/3gpp2",
            "3gp" to "video/3gpp",
            "swf" to "application/x-shockwave-flash",
            "mkv" to "video/x-matroska",
            "mp4" to "video/mp4",
            "avi" to "video/x-msvideo",
            "asf" to "video/x-ms-asf",
            "ts" to "application/octet-stream",
            "webm" to "video/webm",

            "flv" to "video/x-flv",
            "3gpp" to "video/3gpp",
            "mpeg" to "video/mpeg",
            "mov" to "video/quicktime",
            "m4v" to "video/x-m4v",
            "wmv" to "video/x-ms-wmv",
            "asf" to "video/x-ms-asf",
            "mp4v" to "video/mp4v-es",
            "mpg4" to "video/mp4",
            "mpg" to "video/mpeg",
            "m1v" to "video/mpeg",
            "m2v" to "video/mpeg",
            "rm" to "application/vnd.rn-realmedia",
//        "rmvb" to "application/vnd.rn-realmedia-vbr",
            "rmvb" to "video/x-pn-realvideo",
            "ogv" to "video/ogg",
            "f4v" to "video/x-f4v",

            // zip
            "zip" to "application/zip",
            "rar" to "application/x-rar-compressed",
            "7z" to "application/x-7z-compressed",
            "ace" to "application/x-ace-compressed",
            "tar" to "application/x-tar",

            // more
            "323" to "text/h323",
            "bas" to "text/plain",
            "c" to "text/plain",
            "css" to "text/css",
            "etx" to "text/x-setext",
            "h" to "text/plain",
            "htc" to "text/x-component",
            "htt" to "text/webviewhtml",
            "tsv" to "text/tab-separated-values",
            "uls" to "text/iuls",
            "stm" to "text/html",
            "rtx" to "text/richtext",
            "sct" to "text/scriptlet",
            "vcf" to "text/x-vcard",

            "aif" to "audio/x-aiff",
            "aifc" to "audio/x-aiff",
            "aiff" to "audio/x-aiff",
            "au" to "audio/basic",
            "m3u" to "audio/x-mpegurl",
            "snd" to "audio/basic",
            "rmi" to "audio/mid",

            "asr" to "video/x-ms-asf",
            "asx" to "video/x-ms-asf",
            "lsf" to "video/x-la-asf",
            "lsx" to "video/x-la-asf",
            "movie" to "video/x-sgi-movie",
            "mp2" to "video/mpeg",
            "mpa" to "video/mpeg",
            "mpe" to "video/mpeg",
            "mpv2" to "video/mpeg",
            "qt" to "video/quicktime",

            "cmx" to "image/x-cmx",
            "cod" to "image/cis-cod",
            "ief" to "image/ief",
            "jfif" to "image/pipeg",
            "jpe" to "image/jpeg",
            "pnm" to "image/x-portable-anymap",
            "pbm" to "image/x-portable-bitmap",
            "pgm" to "image/x-portable-graymap",
            "ppm" to "image/x-portable-pixmap",
            "ras" to "image/x-cmu-raster",
            "rgb" to "image/x-rgb",
            "svg" to "image/svg+xml",
            "xbm" to "image/x-xbitmap",
            "xpm" to "image/x-xpixmap",
            "xwd" to "image/x-xwindowdump",

            "acx" to "application/internet-property-stream",
            "ai" to "application/postscript",
            "axs" to "application/olescript",
            "bcpio" to "application/x-bcpio",
            "bin" to "application/octet-stream",
            "cat" to "application/vnd.ms-pkiseccat",
            "cdf" to "application/x-cdf",
            "cer" to "application/x-x509-ca-cert",
            "class" to "application/octet-stream",
            "clp" to "application/x-msclip",
            "cpio" to "application/x-cpio",
            "crd" to "application/x-mscardfile",
            "crl" to "application/pkix-crl",
            "crt" to "application/x-x509-ca-cert",
            "csh" to "application/x-csh",
            "dcr" to "application/x-director",
            "der" to "application/x-x509-ca-cert",
            "dir" to "application/x-director",
            "dll" to "application/x-msdownload",
            "dms" to "application/octet-stream",
            "dot" to "application/msword",
            "dvi" to "application/x-dvi",
            "dxr" to "application/x-director",
            "eps" to "application/postscript",
            "evy" to "application/envoy",
            "exe" to "application/octet-stream",
            "fif" to "application/fractals",
            "flr" to "x-world/x-vrml",
            "gtar" to "application/x-gtar",
            "gz" to "application/x-gzip",
            "hdf" to "application/x-hdf",
            "hlp" to "application/winhlp",
            "hqx" to "application/mac-binhex40",
            "hta" to "application/hta",
            "iii" to "application/x-iphone",
            "ins" to "application/x-internet-signup",
            "isp" to "application/x-internet-signup",
            "js" to "application/x-javascript",
            "latex" to "application/x-latex",
            "lha" to "application/octet-stream",
            "lzh" to "application/octet-stream",
            "m13" to "application/x-msmediaview",
            "m14" to "application/x-msmediaview",
            "man" to "application/x-troff-man",
            "mdb" to "application/x-msaccess",
            "me" to "application/x-troff-me",
            "mht" to "message/rfc822",
            "mhtml" to "message/rfc822",
            "mny" to "application/x-msmoney",
            "mpp" to "application/vnd.ms-project",
            "ms" to "application/x-troff-ms",
            "mvb" to "application/x-msmediaview",
            "nws" to "message/rfc822",
            "oda" to "application/oda",
            "p10" to "application/pkcs10",
            "p12" to "application/x-pkcs12",
            "p7b" to "application/x-pkcs7-certificates",
            "p7c" to "application/x-pkcs7-mime",
            "p7m" to "application/x-pkcs7-mime",
            "p7r" to "application/x-pkcs7-certreqresp",
            "p7s" to "application/x-pkcs7-signature",
            "pfx" to "application/x-pkcs12",
            "pko" to "application/ynd.ms-pkipko",
            "pma" to "application/x-perfmon",
            "pmc" to "application/x-perfmon",
            "pml" to "application/x-perfmon",
            "pmr" to "application/x-perfmon",
            "pmw" to "application/x-perfmon",
            "pot," to "application/vnd.ms-powerpoint",
            "pps" to "application/vnd.ms-powerpoint",
            "prf" to "application/pics-rules",
            "ps" to "application/postscript",
            "roff" to "application/x-troff",
            "rtf" to "application/rtf",
            "scd" to "application/x-msschedule",
            "setpay" to "application/set-payment-initiation",
            "setreg" to "application/set-registration-initiation",
            "sh" to "application/x-sh",
            "shar" to "application/x-shar",
            "sit" to "application/x-stuffit",
            "spc" to "application/x-pkcs7-certificates",
            "spl" to "application/futuresplash",
            "src" to "application/x-wais-source",
            "sst" to "application/vnd.ms-pkicertstore",
            "stl" to "application/vnd.ms-pkistl",
            "sv4cpio" to "application/x-sv4cpio",
            "sv4crc" to "application/x-sv4crc",
            "t" to "application/x-troff",
            "tcl" to "application/x-tcl",
            "tex" to "application/x-tex",
            "texi" to "application/x-texinfo",
            "texinfo" to "application/x-texinfo",
            "tgz" to "application/x-compressed",
            "tr" to "application/x-troff",
            "trm" to "application/x-msterminal",
            "ustar" to "application/x-ustar",
            "vrml" to "x-world/x-vrml",
            "wcm" to "application/vnd.ms-works",
            "wdb" to "application/vnd.ms-works",
            "wks" to "application/vnd.ms-works",
            "wmf" to "application/x-msmetafile",
            "wps" to "application/vnd.ms-works",
            "wri" to "application/x-mswrite",
            "wrl" to "x-world/x-vrml",
            "wrz" to "x-world/x-vrml",
            "xaf" to "x-world/x-vrml",
            "xla" to "application/vnd.ms-excel",
            "xlc" to "application/vnd.ms-excel",
            "xlm" to "application/vnd.ms-excel",
            "xlt" to "application/vnd.ms-excel",
            "xlw" to "application/vnd.ms-excel",
            "xof" to "x-world/x-vrml",
            "z" to "application/x-compress"
    )

    val IMAGE_4 = arrayListOf(".bmp", ".gif", ".jpg", ".png", ".dng")
    val IMAGE_5 = arrayListOf(".webp", ".jpeg", ".wbmp")

    val AUDIO_4 = arrayListOf(".3gp", ".aac", ".amr", ".mp3", ".ogg", ".wav", ".m4a", ".mid")
    val AUDIO_5 = arrayListOf(".flac")
    val AUDIOS = mutableListOf<String>().apply {
        addAll(AUDIO_4)
        addAll(AUDIO_5)
    }.map { it.substringAfterLast(".", "") }

    val VIDEO_3 = arrayListOf(".ts")
    val VIDEO_4 = arrayListOf(".3g2", ".3gp", ".avi", ".f4v", ".mkv", ".mov", ".mp4", ".mpg")
    val VIDEO_5 = arrayListOf(".rmvb", ".webm", ".3gpp")

    val DOC_4 = arrayListOf(".htm", ".pdf", ".txt", ".doc", ".ppt", ".xls")
    val DOC_5 = arrayListOf(".epub", ".mobi", ".html", ".docx", ".pptx", ".xlsx")

    val APP_4 = arrayListOf(".apk")

    val ARCHIVE_3 = arrayListOf(".7z")
    val ARCHIVE_4 = arrayListOf(".zip", ".rar")

    fun getDrawable(context: Context, item: DetailItem): Drawable? {
        if (item.isDirectory) {
            return ContextCompat.getDrawable(context, R.drawable.format_folder)
        }

        var resId = when (item.getFileType()) {
            "apk" -> R.drawable.format_app
            "pdf" -> R.drawable.format_pdf
            "html", "htm" -> R.drawable.format_html
            "excel", "xls", "xlsx" -> R.drawable.format_excel
            "ppt", "pptx" -> R.drawable.format_ppt
            "doc", "docx" -> R.drawable.format_word
            "7z", "zip", "rar" -> R.drawable.format_zip
            "txt" -> R.drawable.format_text
            "bt" -> R.drawable.format_torrent
            "ebook" -> R.drawable.format_ebook
            "chm" -> R.drawable.format_chm
            ".fl", ".flash" -> R.drawable.format_flash
            in AUDIOS -> R.drawable.format_audio
            else -> -1
        }

        return if (resId == -1) {
            null
        } else {
            ContextCompat.getDrawable(context, resId)
        }
    }
}