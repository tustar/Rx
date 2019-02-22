package com.tustar.rxjava.ui.okhttp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import com.tustar.rxjava.util.plus
import com.tustar.view.tap
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_ok_http.view.*
import okhttp3.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 *
 */
class OkHttpFragment : Fragment() {

    private var mDisposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ok_http, container,
                false)
        mDisposables + view.sync_get.tap().subscribe {
            syncGet()
        }
        mDisposables + view.async_get.tap().subscribe {
            asyncGet()
        }
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }

    private fun syncGet() {
        Thread {
            val request = Request.Builder()
                    .get()
                    .url("https://publicobject.com/helloworld.txt")
                    .build()
            val client = OkHttpClient()
            try {
                val response = client.newCall(request).execute()
                Logger.d("${response.body()?.string()}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun asyncGet() {
        val request = Request.Builder()
                .get()
                .url("https://publicobject.com/helloworld.txt")
                .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                Logger.d("${response.body()?.string()}")
            }
        })
    }
}
