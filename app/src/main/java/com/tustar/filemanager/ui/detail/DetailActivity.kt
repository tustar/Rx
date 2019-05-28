package com.tustar.filemanager.ui.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.storage.StorageVolume
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.filemanager.model.CachingDocumentFile
import com.tustar.filemanager.utils.FileUtils
import com.tustar.filemanager.utils.StorageUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.toast

class DetailActivity : AppCompatActivity() {

    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        title = getString(R.string.title_activity_files)
        //
        checkPermissionRequest(this)
        //
        val storageVolume = intent.getParcelableExtra(DETAIL_STORAGE_VOLUME) as StorageVolume
        loadStorageVolume(storageVolume)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val directoryUri: Uri
            if (data?.data == null) {
                toast("授权失败")
                finish()
                return
            } else {
                directoryUri = data?.data!!
            }
            val uuid: String? = directoryUri.lastPathSegment?.replace(":", "") ?: return
            if (!this.uuid.equals(uuid, ignoreCase = true)) {
                toast("授权目录不相符")
                finish()
                return
            }

            // Save
            contentResolver?.takePersistableUriPermission(
                    directoryUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            StorageUtils.saveUuidUri(this, uuid!!, directoryUri)

            // Navigation
            showDirectoryContents(directoryUri)
        }
    }

    private fun loadStorageVolume(storageVolume: StorageVolume?) {
        val volume = storageVolume ?: return
        val uuid = (if (volume.isPrimary) {
            "primary"
        } else {
            volume.uuid
        }) ?: return

        this.uuid = uuid

        val uri = StorageUtils.getUuidUri(this, uuid)
        if (uri.isNullOrEmpty()) {
            openDirectory()
        } else {
            showDirectoryContents(uri.toUri())
        }
    }

    fun showDirectoryContents(directoryUri: Uri) {
        supportFragmentManager.transaction {
            val directoryTag = directoryUri.toString()
            val directoryFragment = DetailFragment.newInstance(directoryUri)
            replace(R.id.detail_fragment_container, directoryFragment, directoryTag)
            addToBackStack(directoryTag)
        }
    }

    private fun openDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
    }

    companion object {
        const val DETAIL_STORAGE_VOLUME = "detail_storage_volume"
        const val REQUEST_CODE_OPEN_DIRECTORY = 0xf11e

        fun openDetail(context: Context?, volume: StorageVolume) {
            context?.let {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DETAIL_STORAGE_VOLUME, volume)
                }
                it.startActivity(intent)
            }
        }
    }
}