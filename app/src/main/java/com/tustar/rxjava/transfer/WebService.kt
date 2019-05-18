package com.tustar.rxjava.transfer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.hwangjr.rxbus.RxBus
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.body.MultipartFormDataBody
import com.koushikdutta.async.http.body.Part
import com.koushikdutta.async.http.body.UrlEncodedFormBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.tustar.rxjava.util.Logger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.DecimalFormat


class WebService : Service() {

    private val fileUploadHolder = FileUploadHolder()
    private val server = AsyncHttpServer()
    private val asyncServer = AsyncServer()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_WEB_SERVICE -> startServer()
            ACTION_STOP_WEB_SERVICE -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
        asyncServer.stop()
    }

    private fun startServer() {
        server.get("/", this::onIndex)
        server.get("/css/.*", this::onResources)
        server.get("/images/.*", this::onResources)
        server.get("/scripts/.*", this::onResources)
        server.get("/files", this::onGetUploadList)
        server.post("/files/.*", this::onPostDelete)
        server.get("/files/.*", this::onGetDownload)
        server.post("/files", this::onPostUpload)
        server.get("/progress/.*", this::onGetProgress)
        server.listen(asyncServer, Constants.PORT)
    }

    private fun onIndex(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        try {
            response.send(assets.open("wifi/index.html").use { inputStream ->
                BufferedInputStream(inputStream).use {
                    val baos = ByteArrayOutputStream()
                    it.copyTo(baos, 2048)
                    String(baos.toByteArray(), Charset.defaultCharset())
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            Logger.e(e.message)
            response.code(500).end()
        }
    }


    private fun onResources(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val path = request.path.replace("%20", " ")
        Logger.d("path:$path")
        val resourceName = when {
            path.startsWith("/") -> path.substring(1)
            path.indexOf("?") > 0 -> path.substring(0, path.indexOf("?"))
            else -> path
        }

        val contentType = ServerUtils.getContentTypeByResourceName(resourceName)
        if (contentType.isNullOrEmpty()) {
            response.setContentType(contentType)
        }
        assets.open("wifi/$resourceName").use { inputStream ->
            BufferedInputStream(inputStream).use {
                response.sendStream(it, it.available().toLong())
            }
        }
    }

    private fun onGetUploadList(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val array = JSONArray()
        val dir = Constants.DIR
        if (dir.exists() && dir.isDirectory) {
            val fileNames = dir.list()
            if (fileNames != null) {
                for (fileName in fileNames) {
                    val file = File(dir, fileName)
                    if (file.exists() && file.isFile) {
                        try {
                            val jsonObject = JSONObject()
                            jsonObject.put("name", fileName)
                            val fileLen = file.length()
                            val df = DecimalFormat("0.00")
                            when {
                                fileLen > 1024 * 1024 -> jsonObject.put("size",
                                        df.format(fileLen * 1f / 1024f / 1024f) + "MB")
                                fileLen > 1024 -> jsonObject.put("size", df.format(fileLen * 1f / 1024) + "KB")
                                else -> jsonObject.put("size", "${fileLen}B")
                            }
                            array.put(jsonObject)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
        response.send(array.toString())
    }

    private fun onPostDelete(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val body = request.body as UrlEncodedFormBody
        if ("delete".equals(body.get().getString("_method"), ignoreCase = true)) {
            var path = request.path.replace("/files/", "")
            try {
                path = URLDecoder.decode(path, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            val file = File(Constants.DIR, path)
            if (file.exists() && file.isFile && file.delete()) {
                RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
            }
        }
        response.end()
    }

    private fun onGetDownload(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        var path = request.path.replace("/files/", "")
        try {
            path = URLDecoder.decode(path, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        val file = File(Constants.DIR, path)
        if (file.exists() && file.isFile) {
            try {
                response.headers.add("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(file.name, "utf-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            response.sendFile(file)
            return
        }
        response.code(404).send("Not found!")
    }

    private fun onPostUpload(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val body = request.body as MultipartFormDataBody
        body.setMultipartCallback { part: Part ->
            if (part.isFile) {
                body.setDataCallback { _, bb ->
                    fileUploadHolder.write(bb.allByteArray)
                    bb.recycle()
                }
            } else {
                if (body.dataCallback == null) {
                    body.setDataCallback { _, bb ->
                        try {
                            val fileName = URLDecoder.decode(String(bb
                                    .allByteArray), "UTF-8")
                            fileUploadHolder.fileName = fileName
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                        bb.recycle()
                    }
                }
            }
        }
        request.setEndCallback {
            fileUploadHolder.reset()
            response.end()
            RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
        }
    }

    private fun onGetProgress(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val path = request.path.replace("/progress/", "")
        val res = JSONObject()
        if (path == fileUploadHolder.fileName) {
            try {
                res.put("fileName", fileUploadHolder.fileName)
                res.put("size", fileUploadHolder.totalSize)
                res.put("progress",
                        if (fileUploadHolder.fileOutputStream == null) {
                            1
                        } else {
                            0.1
                        })
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        response.send(res)
    }

    companion object {
        private const val ACTION_PREFIX = "com.tustar.rxjava.action"
        const val ACTION_START_WEB_SERVICE = "$ACTION_PREFIX.START_WEB_SERVICE"
        const val ACTION_STOP_WEB_SERVICE = "$ACTION_PREFIX.STOP_WEB_SERVICE"

        fun start(context: Context) {
            val intent = Intent(context, WebService::class.java).apply {
                action = ACTION_START_WEB_SERVICE
            }
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, WebService::class.java).apply {
                action = ACTION_STOP_WEB_SERVICE
            }
            context.startService(intent)
        }
    }

    inner class FileUploadHolder {
        var fileName: String? = null
            internal set(value) {
                field = value
                totalSize = 0
                if (!Constants.DIR.exists()) {
                    Constants.DIR.mkdirs()
                }
                this.receivedFile = File(Constants.DIR, this.fileName)
                Timber.d(receivedFile!!.absolutePath)
                try {
                    fileOutputStream = BufferedOutputStream(FileOutputStream(receivedFile))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        private var receivedFile: File? = null
        var fileOutputStream: BufferedOutputStream? = null
            private set
        var totalSize: Long = 0

        fun reset() {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            fileOutputStream = null
        }

        fun write(data: ByteArray) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream!!.write(data)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            totalSize += data.size.toLong()
        }
    }
}

