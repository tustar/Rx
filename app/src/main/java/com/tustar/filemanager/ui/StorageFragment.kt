package com.tustar.filemanager.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageVolume
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.StorageType
import com.tustar.filemanager.model.StorageItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.filemanager.utils.StorageUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.fragment_storage.*
import org.jetbrains.anko.support.v4.toast


class StorageFragment : Fragment(), OnItemClickListener<StorageItem> {

    private lateinit var storageAdapter: StorageAdapter
    private var uuid: String? = null
    private var volume: StorageVolume? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        context?.let {
            storageAdapter = StorageAdapter(StorageItem.getStorageItems(it))
            storageAdapter.onItemClickListener = this
            val linearLayoutManager = LinearLayoutManager(it)
            with(main_storage_recyclerView) {
                adapter = storageAdapter
                layoutManager = linearLayoutManager
                addItemDecoration(DividerItemDecoration(it, linearLayoutManager.orientation))
            }
        }
    }

    override fun onItemClick(item: StorageItem) {
        val volume = item.volume
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uuid = if (volume.isPrimary) {
                "primary"
            } else {
                item.volume.uuid
            }
            Logger.d("uuid:$uuid")
            context?.let { context ->
                uuid?.let { uuid ->
                    val uri = StorageUtils.getUuidUri(context, uuid)
                    if (uri == null) {
                        openDirectory()
                    } else {
                        openDetail(Uri.parse(uri))
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val directoryUri: Uri
            if (data?.data == null) {
                toast("授权失败")
                return
            } else {
                directoryUri = data?.data
            }
            var uuid = directoryUri.lastPathSegment.replace(":", "")
            if (!this.uuid.equals(uuid, ignoreCase = true)) {
                toast("授权目录不相符")
                return
            }

            // Save
            context?.contentResolver?.takePersistableUriPermission(
                    directoryUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            context?.let {
                StorageUtils.saveUuidUri(it, uuid, directoryUri)
            }

            // Navigation
            openDetail(directoryUri)
        }
    }

    private fun openDetail(directoryUri: Uri) {
        var type = StorageType.EMMC
        DetailActivity.openDetail(context, type, directoryUri)
    }

    private fun openDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    companion object {

        @JvmStatic
        fun newInstance() = StorageFragment()

        const val OPEN_DIRECTORY_REQUEST_CODE = 0xf11e
    }
}
