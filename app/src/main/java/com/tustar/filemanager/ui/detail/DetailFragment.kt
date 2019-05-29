package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_detail.*

private const val ARG_DIRECTORY_URI = "arg_directory_uri"

open class DetailFragment : Fragment(), OnItemClickListener<DetailFileItem> {

    protected lateinit var viewModel: DetailViewModel
    protected lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onItemClick(clickedDocument: DetailFileItem) {
        viewModel.documentClicked(clickedDocument)
    }

    private fun initViews() {
        detailAdapter = DetailAdapter(this)
        detail_recyclerView.apply {
            setHasFixedSize(true)
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,
                    LinearLayoutManager(context).orientation))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
    }
}
