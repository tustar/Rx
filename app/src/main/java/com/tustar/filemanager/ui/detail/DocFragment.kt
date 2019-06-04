package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R


class DocFragment : DetailFragment() {

    private lateinit var viewModel: DocViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DocViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadDocs()
    }

    override fun initCurrentNaviItem() {
        super.initCurrentNaviItem()
        currentNaviItem = DetailNaviItem(getString(R.string.category_doc))
    }

    override fun onItemClick(item: DetailFileItem) {
        super.onItemClick(item)
        item.uri?.let {
            FileUtils.openDocument(context, it)
        }
    }

    override fun reload() {
        super.reload()
        viewModel.loadDocs()
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { contentAdapter.setEntries(documents) }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = DocFragment()
    }
}
