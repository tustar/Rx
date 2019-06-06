package com.tustar.filemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.base.BaseStorageFragment
import com.tustar.filemanager.model.DetailItem
import com.tustar.filemanager.model.DetailNaviItem
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.base.OnLongItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.fragment_detail.*

abstract class DetailFragment : BaseStorageFragment(), OnItemClickListener<DetailItem>,
        OnLongItemClickListener<DetailItem>, DetailNaviAdapter.OnNaviItemClickListener {

    protected lateinit var contentAdapter: DetailContentAdapter
    protected lateinit var naviAdapter: DetailNaviAdapter
    protected var currentNaviItem: DetailNaviItem? = null
    protected var isInEditModel = false
        set(value) {
            field = value
            naviAdapter.isInEditModel = value
            naviAdapter.notifyDataSetChanged()

            //
            contentAdapter.isInEditModel = value
            contentAdapter.notifyDataSetChanged()

            //
            detail_tool_bar.visibility = if(value) {
                View.VISIBLE
            }  else {
                View.GONE
            }
        }
    protected var checkedItems = mutableListOf<DetailItem>()

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

    override fun onItemClick(item: DetailItem) {
        if (isInEditModel) {
            if (item.isChecked) {
                checkedItems.add(item)
            }
            updateToolbar()
            return
        }
    }

    open fun updateToolbar() {
        if (checkedItems.size == 1) {
            toolbar_item_detail.visibility = View.VISIBLE
        } else {
            toolbar_item_detail.visibility = View.GONE
        }
    }

    override fun onLongItemClick(item: DetailItem) {
        isInEditModel = true
        checkedItems.clear()
        checkedItems.add(item)
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
        if (isInEditModel) {
            isInEditModel = false
            return false
        }

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
        contentAdapter = DetailContentAdapter(this, this)
        detail_recyclerView.apply {
            setHasFixedSize(true)
            adapter = contentAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,
                    LinearLayoutManager(context).orientation))
        }

        initToolbar()
    }

    protected fun initToolbar() {
        toolbar_item_delete.setOnClickListener {

        }

        toolbar_item_copy.setOnClickListener {

        }

        toolbar_item_cut.setOnClickListener {

        }

        toolbar_item_share.setOnClickListener {
            FileUtils.share(context, checkedItems)
        }
    }

    abstract fun initCurrentNaviItem()
    abstract fun reload()
}
