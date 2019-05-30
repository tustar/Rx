package com.tustar.filemanager.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.utils.FileUtils

class VolumeFragment : DetailFragment() {

    private lateinit var directoryUri: Uri
    private lateinit var viewModel: VolumeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryUri = arguments?.getParcelable(ARG_DIRECTORY_URI)
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        viewModel = ViewModelProviders.of(this).get(VolumeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadDirectory(directoryUri)
    }

    override fun onItemClick(item: DetailFileItem) {
        viewModel.documentClicked(item)
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents) }
        })

        viewModel.openDirectory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { directory ->
                (activity as? DetailActivity)?.showDetailContent(directory.uri!!)
            }
        })

        viewModel.openDocument.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                FileUtils.openDocument(context, document)
            }
        })
    }

    companion object {

        private const val ARG_DIRECTORY_URI = "arg_directory_uri"

        @JvmStatic
        fun newInstance(directoryUri: Uri) = VolumeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_DIRECTORY_URI, directoryUri)
            }
        }
    }
}
