package com.tustar.filemanager.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.base.OnItemClickListener

private const val ARG_DIRECTORY_URI = "arg_directory_uri"

class VolumeFragment : DetailFragment(), OnItemClickListener<DetailFileItem> {

    private lateinit var directoryUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryUri = arguments?.getString(ARG_DIRECTORY_URI)?.toUri()
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadDirectory(directoryUri)
    }

    override fun onItemClick(clickedDocument: DetailFileItem) {
        viewModel.documentClicked(clickedDocument)
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents) }
        })

        viewModel.openDirectory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { directory ->
                (activity as? DetailActivity)?.showDirectoryContents(directory.uri!!)
            }
        })

        viewModel.openDocument.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                FileUtils.openDocument(context, document)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(directoryUri: Uri) = VolumeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DIRECTORY_URI, directoryUri.toString())
            }
        }
    }
}
