package com.tustar.filemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.model.StorageItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_storage.*


class StorageFragment : Fragment(), OnItemClickListener<StorageItem> {

    private lateinit var storageAdapter: StorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        context?.let {
            storageAdapter = StorageAdapter(StorageItem.getStorageItems(it))
            storageAdapter.onItemClickListener = this
            val linearLayoutManager = LinearLayoutManager(it)
            with(main_storage_recyclerView) {
                adapter = storageAdapter
                layoutManager = linearLayoutManager
                addItemDecoration(DividerItemDecoration(it, linearLayoutManager.orientation))
            }
        }
    }

    override fun onItemClick(item: StorageItem) {
        DetailActivity.openDetail(context, item.volume)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StorageFragment()
    }
}
