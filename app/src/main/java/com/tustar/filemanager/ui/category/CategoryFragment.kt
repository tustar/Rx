package com.tustar.filemanager.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.CategoryItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.filemanager.ui.detail.DetailParams
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : Fragment(), OnItemClickListener<CategoryItem> {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        loadData()
    }

    private fun initViews() {
        categoryAdapter = CategoryAdapter()
        categoryAdapter.onItemClickListener = this
        with(main_category_gridview) {
            adapter = categoryAdapter
        }
    }

    private fun initObservers() {
        viewModel.categories.observe(this, Observer {
            categoryAdapter.items = it
        })
        viewModel.imageCount.observe(this, Observer {
            categoryAdapter.items[INDEX_IMAGE].count = it
            categoryAdapter.notifyDataSetChanged()
        })
        viewModel.audioCount.observe(this, Observer {
            categoryAdapter.items[INDEX_AUDIO].count = it
            categoryAdapter.notifyDataSetChanged()
        })
        viewModel.videoCount.observe(this, Observer {
            categoryAdapter.items[INDEX_VIDEO].count = it
            categoryAdapter.notifyDataSetChanged()
        })
        viewModel.docCount.observe(this, Observer {
            categoryAdapter.items[INDEX_DOC].count = it
            categoryAdapter.notifyDataSetChanged()
        })
        viewModel.appCount.observe(this, Observer {
            categoryAdapter.items[INDEX_APP].count = it
            categoryAdapter.notifyDataSetChanged()
        })
        viewModel.archivesCount.observe(this, Observer {
            categoryAdapter.items[INDEX_ARCHIVES].count = it
            categoryAdapter.notifyDataSetChanged()
        })
    }

    private fun loadData() {
        viewModel.loadCategories()
        viewModel.loadImageBucketCount()
        viewModel.loadAudioCount()
        viewModel.loadVideoCount()
        viewModel.loadDocCount()
        viewModel.loadAppCount()
        viewModel.loadArchivesCount()
    }

    override fun onItemClick(item: CategoryItem) {
        DetailActivity.openVolumeDetail(context, DetailParams(
                type = item.type,
                name = getString(item.name)))
    }

    companion object {

        const val INDEX_IMAGE = 0
        const val INDEX_AUDIO = 1
        const val INDEX_VIDEO = 2
        const val INDEX_DOC = 3
        const val INDEX_APP = 4
        const val INDEX_ARCHIVES = 5

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
