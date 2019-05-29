package com.tustar.filemanager.ui.volume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.annotation.TYPE_STORAGE_PHONE
import com.tustar.filemanager.model.VolumeItem
import com.tustar.filemanager.ui.detail.DetailActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_volume.*


class VolumeFragment : Fragment(), OnItemClickListener<VolumeItem> {

    private lateinit var volumeAdapter: VolumeAdapter
    private lateinit var viewModel: VolumeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        viewModel = ViewModelProviders.of(this).get(VolumeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        viewModel.getVolumes()
    }

    private fun initViews() {
        volumeAdapter = VolumeAdapter()
        context?.let {
            volumeAdapter.onItemClickListener = this
            val linearLayoutManager = LinearLayoutManager(it)
            with(main_volume_recyclerView) {
                adapter = volumeAdapter
                layoutManager = linearLayoutManager
                addItemDecoration(DividerItemDecoration(it, linearLayoutManager.orientation))
            }
        }
    }

    private fun initObservers() {
        viewModel.volumes.observe(this, Observer {
            volumeAdapter.items = it
        })
    }

    override fun onItemClick(item: VolumeItem) {
        DetailActivity.openDetail(context, item.volume, TYPE_STORAGE_PHONE)
    }

    companion object {
        @JvmStatic
        fun newInstance() = VolumeFragment()
    }
}
