package com.tustar.filemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tustar.filemanager.model.CategoryItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : Fragment(), OnItemClickListener<CategoryItem> {

    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        categoryAdapter = CategoryAdapter(CategoryItem.getCategoryItems())
        categoryAdapter.onItemClickListener = this
        with(main_category_gridview) {
            adapter = categoryAdapter
        }
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
