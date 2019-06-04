package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.fragment_detail.*

abstract class DetailFragment : Fragment(), OnItemClickListener<DetailFileItem>,
        DetailNaviAdapter.OnNaviItemClickListener {

    protected lateinit var contentAdapter: DetailContentAdapter
    protected lateinit var naviAdapter: DetailNaviAdapter
    protected var currentNaviItem: DetailNaviItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCurrentNaviItem()

        context?.let {
            naviAdapter.pushNaviItem(DetailNaviItem.getNaviHome(it))
        }
        currentNaviItem?.let {
            naviAdapter.pushNaviItem(it)
        }
    }

    override fun onItemClick(item: DetailFileItem) {

    }

    override fun onNaviItemClick(item: DetailNaviItem) {
        Logger.i("item:$item")
        when {
            item.isHome -> activity?.finish()
            item == currentNaviItem -> reload()
            else -> {
                naviAdapter.popToNaviItem(item)
                detail_navi_layout.scrollToPosition(naviAdapter.itemCount)
            }
        }
    }

    fun onBackPressed(): Boolean {
        if (naviAdapter.itemCount <= 2) {
            return true
        }

        naviAdapter.pop()
        currentNaviItem = naviAdapter.naviEntries.last
        reload()
        detail_navi_layout.scrollToPosition(naviAdapter.itemCount)
        return false
    }

    private fun initViews() {
        // Navi
        naviAdapter = DetailNaviAdapter(this)
        detail_navi_layout.apply {
            adapter = naviAdapter
        }

        // Content
        contentAdapter = DetailContentAdapter(this)
        detail_recyclerView.apply {
            setHasFixedSize(true)
            adapter = contentAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,
                    LinearLayoutManager(context).orientation))
        }
    }

    abstract fun initCurrentNaviItem()
    abstract fun reload()
}
