package com.tustar.rxjava.ui.transfer

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.rxjava.transfer.Constants
import com.tustar.rxjava.transfer.WebService
import com.tustar.rxjava.util.Logger
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_browser_transfer.*


class BrowserTransferActivity : AppCompatActivity() {

    private var disposables = CompositeDisposable()
    private var fileBeanList = arrayListOf<FileModel>()
    private lateinit var transferAdapter: TransferAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tustar.rxjava.R.layout.activity_browser_transfer)
        title = "Browser Transfer"

        checkPermissionRequest(this)
        WebService.start(this)
        RxBus.get().register(this)

        initRecycleView()
    }

    private fun initRecycleView() {
        transferAdapter = TransferAdapter(fileBeanList)
        transfer_recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = transferAdapter
        }
        RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
    }

    @Subscribe(thread = EventThread.IO, tags = [Tag(Constants.RxBusEventType.LOAD_FILE_LIST)])
    fun loadFileList(type: Int?) {
        val dir = Constants.DIR
        if (dir.exists() && dir.isDirectory) {
            val files = dir.listFiles() ?: return
            fileBeanList.clear()
            files.sortByDescending { it.lastModified() }
            fileBeanList.addAll(files
                    .filter { !it.name.startsWith(".") }
                    .map { FileModel.toModel(this, it) }
                    .toList())
            transferAdapter.files = fileBeanList
        }
        runOnUiThread {
            transferAdapter.notifyDataSetChanged()
        }
    }

    private fun checkPermissionRequest(activity: FragmentActivity) {
        val permissions = RxPermissions(activity)
        permissions.setLogging(true)
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    Logger.d("granted = $granted")
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        RxBus.get().unregister(this)
    }
}
