package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.filemanager.ui.detail.DetailActivity.Companion.ARG_DETAIL_PARAMS
import com.tustar.filemanager.utils.FileUtils

class VolumeFragment : DetailFragment() {

    private lateinit var params: DetailParams
    private lateinit var viewModel: VolumeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        params = arguments?.getParcelable(ARG_DETAIL_PARAMS)
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        viewModel = ViewModelProviders.of(this).get(VolumeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        params.directoryUri?.let {
            viewModel.loadDirectory(it)
        }
    }

    override fun initCurrentNaviItem() {
        super.initCurrentNaviItem()
        params.volumeName?.let {
            currentNaviItem = DetailNaviItem(
                    name = it,
                    uri = params.directoryUri)
        }
    }

    override fun onItemClick(item: DetailFileItem) {
        super.onItemClick(item)
        viewModel.documentClicked(item)
    }

    override fun onNaviItemClick(item: DetailNaviItem) {
        super.onNaviItemClick(item)

        if (item == currentNaviItem) {
            currentNaviItem?.uri?.let {
                viewModel.loadDirectory(it)
            }
        }
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { contentAdapter.setEntries(documents) }
        })

        viewModel.openDirectory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { directory ->
                viewModel.loadDirectory(directory.uri!!)
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
        fun newInstance(params: DetailParams) = VolumeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_DETAIL_PARAMS, params)
            }
        }
    }
}
