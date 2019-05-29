package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.ImageBucketFileItem
import com.tustar.rxjava.util.Logger

class ImageBucketFragment : DetailFragment() {

    private lateinit var viewModel: ImageBucketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ImageBucketViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.loadImageBucket()
    }

    override fun onItemClick(item: DetailFileItem) {
        if (item is ImageBucketFileItem) {
            (activity as? DetailActivity)?.showDetailContent(bucketId = item.bucketId)
        }
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents) }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ImageBucketFragment()
    }
}
