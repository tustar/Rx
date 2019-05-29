package com.tustar.filemanager.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tustar.filemanager.model.CategoryItem
import com.tustar.filemanager.ui.detail.DetailActivity
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
        viewModel.loadCategories()
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
    }

    override fun onItemClick(item: CategoryItem) {
        val intent = Intent(context!!, DetailActivity::class.java).apply {

        }
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
