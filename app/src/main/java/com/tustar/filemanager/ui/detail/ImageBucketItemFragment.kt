package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.util.Logger


class ImageBucketItemFragment : DetailFragment() {

    private var bucketId: Long? = null
    private lateinit var viewModel: ImageBucketItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bucketId = arguments?.getLong(ARG_BUCKET_ID)
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        viewModel = ViewModelProviders.of(this).get(ImageBucketItemViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        bucketId?.let {
            viewModel.loadImageByBucketId(it)
        }
    }

    override fun onItemClick(item: DetailFileItem) {
        item.uri?.let {
            FileUtils.openDocument(context, it)
        }
    }

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents) }
        })
    }

    companion object {
        private const val ARG_BUCKET_ID = "arg_bucket_id"

        @JvmStatic
        fun newInstance(bucketId: Long) = ImageBucketItemFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_BUCKET_ID, bucketId)
            }
        }
    }
}
