package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R


class AppFragment : DetailFragment() {

    private lateinit var viewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadApps()
    }

    override fun initCurrentNaviItem() {
        currentNaviItem = DetailNaviItem(getString(R.string.category_app))
    }

    override fun onItemClick(item: DetailFileItem) {
        super.onItemClick(item)
        item.uri?.let {
            FileUtils.openDocument(context, it)
        }
    }

    override fun reload() {
        viewModel.loadApps()
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { contentAdapter.setEntries(documents) }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = AppFragment()
    }
}
