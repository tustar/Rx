package com.tustar.filemanager.ui.volume

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.annotation.TYPE_STORAGE_PHONE
import com.tustar.filemanager.extension.uid
import com.tustar.filemanager.model.VolumeItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.filemanager.utils.Constants.EXTERNAL_STORAGE_PROVIDER_AUTHORITY
import com.tustar.filemanager.utils.StorageUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.fragment_volume.*
import org.jetbrains.anko.support.v4.toast


class VolumeFragment : Fragment(), OnItemClickListener<VolumeItem> {

    private lateinit var volumeAdapter: VolumeAdapter
    private lateinit var viewModel: VolumeViewModel
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        viewModel = ViewModelProviders.of(this).get(VolumeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        viewModel.getVolumes()
    }

    private fun initViews() {
        volumeAdapter = VolumeAdapter()
        context?.let {
            volumeAdapter.onItemClickListener = this
            val linearLayoutManager = LinearLayoutManager(it)
            with(main_volume_recyclerView) {
                adapter = volumeAdapter
                layoutManager = linearLayoutManager
                addItemDecoration(DividerItemDecoration(it, linearLayoutManager.orientation))
            }
        }
    }

    private fun initObservers() {
        viewModel.volumes.observe(this, Observer {
            volumeAdapter.items = it
        })
    }

    override fun onItemClick(item: VolumeItem) {
        val volume = item.volume
        val uuid = volume.uid()

        context?.let { context ->
            if (uuid != null) {
                val directoryUri = StorageUtils.getUuidUri(context, uuid)?.toUri()
                if (directoryUri == null) {
                    openDirectory(uuid)
                } else {
                    openVolumeDetail(directoryUri)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val directoryUri: Uri
            if (data?.data == null) {
                toast("授权失败")
                return
            } else {
                directoryUri = data?.data!!
            }
            val uuid: String? = directoryUri.lastPathSegment?.replace(":", "")
            if (uuid == null) {
                toast("授权目录不相符")
                return
            }

            if (!this.uuid.equals(uuid, ignoreCase = true)) {
                toast("授权目录不相符")
                return
            }

            // Save
            context?.contentResolver?.takePersistableUriPermission(
                    directoryUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            StorageUtils.saveUuidUri(context!!, uuid!!, directoryUri)

            // Navigation
            openVolumeDetail(directoryUri)
        }
    }

    private fun openDirectory(uuid: String) {
        this.uuid = uuid

        val volumeUri = DocumentsContract.buildRootUri(
                EXTERNAL_STORAGE_PROVIDER_AUTHORITY, uuid)
        Logger.d("volumeUri:$volumeUri")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, volumeUri)
        }

        startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
    }

    private fun openVolumeDetail(directoryUri: Uri) {
        DetailActivity.openVolumeDetail(context, TYPE_STORAGE_PHONE, directoryUri)
    }

    companion object {

        const val REQUEST_CODE_OPEN_DIRECTORY = 0xf11e

        @JvmStatic
        fun newInstance() = VolumeFragment()
    }
}
