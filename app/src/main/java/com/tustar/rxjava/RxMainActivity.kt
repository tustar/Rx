package com.tustar.rxjava

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.ui.main.MainContent
import com.tustar.rxjava.util.AsyncTask
import com.tustar.rxjava.util.Logger
import org.jetbrains.anko.toast
import java.util.concurrent.atomic.AtomicInteger


class RxMainActivity : AppCompatActivity(), OnItemClickListener<MainContent.DummyItem> {

    private lateinit var mNavController: NavController
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_main)
        mNavController = findNavController(R.id.nav_host_fragment)

        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        DownloadTask().execute()
    }

    override fun onSupportNavigateUp() = mNavController.navigateUp()

    override fun onItemClick(item: MainContent.DummyItem) {
        mNavController.navigate(item.action)
        title = item.content
    }

    override fun onResume() {
        super.onResume()
        Logger.i()
    }

    override fun onPause() {
        super.onPause()
        Logger.i()
    }

    override fun onStop() {
        super.onStop()
        Logger.i()
    }

    companion object {
        const val INTENT_KEY_FROM_WHICH = "intent_key_from_which"
        const val INTENT_KEY_NOTIFICATION_ID = "intent_key_notification_id"
        const val ACTION_CLOSE_NOTIFICATION = "com.tustar.rxjava.ACTION_CLOSE_NOTIFICATION"
    }

    inner class DownloadTask : AsyncTask<Unit, Int, Boolean>() {

        override fun onPreExecute() {
            progressDialog.show()
            progressDialog.setMessage("准备下载")
        }

        override fun doInBackground(vararg params: Unit): Boolean {
            try {
                while (true) {
                    val downloadPercent = doDownload()
                    Logger.d("downloadPercent: $downloadPercent%")
                    publishProgress(downloadPercent)
                    if (downloadPercent >= 100) {
                        break
                    }
                }
            } catch (e: Exception) {
                return false
            }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            progressDialog.setMessage("当前下载进度：${values[0]}%")
            progressDialog.progress = values[0] ?: 0
        }

        override fun onPostExecute(result: Boolean) {
            progressDialog.dismiss()
            val message = if (result) "下载成功" else "下载失败"
            toast(message)
        }

        private var percent = AtomicInteger(1)
        private fun doDownload(): Int {
            try {
                Thread.sleep(20L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return percent.getAndIncrement()
        }
    }
}
