package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.filemanager.model.ImageFileItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R

class ImageFragment : DetailFragment() {

    private lateinit var viewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ImageViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadImageBucket()
    }

    override fun initCurrentNaviItem() {
        currentNaviItem = DetailNaviItem(getString(R.string.category_image))
    }

    override fun onItemClick(item: DetailFileItem) {
        super.onItemClick(item)
        if (item is ImageFileItem) {
            when (item.isBucket) {
                true -> {
                    viewModel.loadImageByBucketId(item.bucketId!!)
                    currentNaviItem = DetailNaviItem(
                            name = item.bucketName!!,
                            bucketId = item.bucketId,
                            uri = item.uri)
                    naviAdapter.pushNaviItem(currentNaviItem!!)
                }
                else -> {
                    item.uri?.let {
                        FileUtils.openDocument(context, it)
                    }
                }
            }
        }
    }

    override fun reload() {
        currentNaviItem?.run {
            if (bucketId == null) {
                viewModel.loadImageBucket()
            } else {
                viewModel.loadImageByBucketId(bucketId)
            }
        }
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { contentAdapter.setEntries(documents) }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageFragment()
    }
}