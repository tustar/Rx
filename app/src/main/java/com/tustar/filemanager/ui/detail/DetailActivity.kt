package com.tustar.filemanager.ui.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.filemanager.StorageType
import com.tustar.filemanager.model.CachingDocumentFile
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnItemClickListener<CachingDocumentFile> {

    private lateinit var storageType: StorageType
    private lateinit var directoryUri: Uri
    private lateinit var viewModel: DetailViewModel
    private lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        title = getString(R.string.title_activity_files)

        directoryUri = intent.getStringExtra(DETAIL_DIRECTORY_URI)?.toUri()
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        checkPermissionRequest(this)
        initViews()

        initObservers()
    }

    override fun onItemClick(clickedDocument: CachingDocumentFile) {
        viewModel.documentClicked(clickedDocument)
    }

    private fun initViews() {
        detailAdapter = DetailAdapter(this)
        detail_recyclerView.apply {
            setHasFixedSize(true)
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,
                    LinearLayoutManager(context).orientation))
        }

        viewModel.loadDirectory(directoryUri)
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents)}
        })

        viewModel.openDirectory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { directory ->
                viewModel.loadDirectory(directory.uri )
            }
        })

        viewModel.openDocument.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                FileUtils.openDocument(this, document)
            }
        })
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
        super.onBackPressed()
    }

    companion object {

        const val DETAIL_STORAGE_TYPE = "detail_storage_type"
        const val DETAIL_DIRECTORY_URI = "detail_directory_uri"

        fun openDetail(context: Context?, type: StorageType, directoryUri: Uri) {
            context?.let {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DETAIL_STORAGE_TYPE, type)
                    putExtra(DETAIL_DIRECTORY_URI, directoryUri.toString()+ "/360")
                }
                it.startActivity(intent)
            }
        }
    }
}
