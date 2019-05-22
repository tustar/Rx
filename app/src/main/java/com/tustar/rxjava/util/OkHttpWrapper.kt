package com.tustar.rxjava.util

import android.annotation.SuppressLint
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OkHttpWrapper {

    var url: String? = null
    var method: String? = null
    var body: RequestBody? = null
    var timeout: Long = 10L

    internal var success: (String) -> Unit = { }
    internal var fail: (Throwable) -> Unit = {}

    fun onSuccess(onSuccess: (String) -> Unit) {
        success = onSuccess
    }

    fun onFail(onError: (Throwable) -> Unit) {
        fail = onError
    }
}

fun http(init: OkHttpWrapper.() -> Unit) {
    val wrap = OkHttpWrapper()

    wrap.init()

    executeForResult(wrap)
}

@SuppressLint("CheckResult")
private fun executeForResult(wrap: OkHttpWrapper) {

    Flowable.create<Response>({ e ->
        e.onNext(onExecute(wrap)!!)
    }, BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .subscribe(
                    { resp ->
                        wrap.success(resp.body()!!.string())
                    },
                    { e ->
                        wrap.fail(e)
                    })
}

private fun onExecute(wrap: OkHttpWrapper): Response? {
    val request: Request? = when (wrap.method) {
        "get", "Get", "GET" -> Request.Builder().url(wrap.url).build()
        "post", "Post", "POST" -> Request.Builder().url(wrap.url).post(wrap.body).build()
        "put", "Put", "PUT" -> Request.Builder().url(wrap.url).put(wrap.body).build()
        "delete", "Delete", "DELETE" -> Request.Builder().url(wrap.url).delete(wrap.body).build()
        else -> null
    }
    val client = OkHttpClient.Builder()
            .connectTimeout(wrap.timeout, TimeUnit.SECONDS)
            .build()
    return client.newCall(request).execute()
}