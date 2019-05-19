package com.tustar.rxjava.ui.files

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.files.WebService
import com.tustar.rxjava.util.Constants
import com.tustar.rxjava.util.FileUtils
import com.tustar.rxjava.util.Logger
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_file_manager.*
import java.io.File
import java.io.IOException

class FilesActivity : AppCompatActivity(), OnItemClickListener<FileModel> {

    private var disposables = CompositeDisposable()
    private var fileBeanList = arrayListOf<FileModel>()
    private lateinit var transferAdapter: FileAdapter
    private var dir: File = Constants.DIR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)
        title = getString(R.string.title_activity_files)

        checkPermissionRequest(this)
        WebService.start(this)
        RxBus.get().register(this)

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_transfer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_files -> {
                val intent = Intent().apply {
                    action = Intent.ACTION_GET_CONTENT
                    type = "*/*"
                }
                startActivityForResult(intent, FILE_FETCH_CODE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_FETCH_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                try {
                    FileUtils.copyFile(contentResolver.openInputStream(uri),
                            "${Constants.DIR}${File.separator}${FileUtils.getFileName(this, uri)}")
                    Toast.makeText(this, R.string.files_please_refresh_web, Toast.LENGTH_LONG).show()
                    RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
                } catch (e: IOException) {
                    Toast.makeText(this, R.string.files_read_file_failed, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.files_read_file_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        RxBus.get().unregister(this)
    }

    override fun onItemClick(item: FileModel) {
        item.path?.let {
            if (item.isDirectory) {
                dir = File(it)
                RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
            } else {
                FileUtils.openFile(this, it)
            }
        }
    }

    private fun initViews() {
        transferAdapter = FileAdapter(fileBeanList, this)
        files_recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = transferAdapter
            addItemDecoration(DividerItemDecoration(context,
                    LinearLayoutManager(context).orientation))
        }
        RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)

        files_swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        files_swipeRefreshLayout.setOnRefreshListener {
            RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
        }

//        transfer_fab.setOnClickListener {
//            val objectAnimator = ObjectAnimator.ofFloat(transfer_fab, "translationY", 0, transfer_fab
//                    .height * 2.0F).setDuration(200L)
//            objectAnimator.setInterpolator(AccelerateInterpolator())
//            objectAnimator.addListener(this)
//            objectAnimator.start()
//        }
    }

    @Subscribe(thread = EventThread.IO, tags = [Tag(Constants.RxBusEventType.LOAD_FILE_LIST)])
    fun loadFileList(type: Int?) {
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
            files_swipeRefreshLayout.isRefreshing = false
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

    override fun onBackPressed() {
        val parent = dir.parentFile
        if(parent== Environment.getExternalStorageDirectory()) {
            super.onBackPressed()
        } else {
            dir = parent
            RxBus.get().post(Constants.RxBusEventType.LOAD_FILE_LIST, 0)
        }
    }

    companion object {
        const val FILE_FETCH_CODE = 0x1
    }
}
