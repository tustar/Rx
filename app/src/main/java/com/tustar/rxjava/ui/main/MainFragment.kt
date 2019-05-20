package com.tustar.rxjava.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.rxjava.MainActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.ui.main.MainContent.DummyItem

class MainFragment : androidx.fragment.app.Fragment() {

    private var columnCount = 1

    private var listener: OnItemClickListener<DummyItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_list, container, false)

        // Set the adapter
        if (view is androidx.recyclerview.widget.RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MainRecyclerViewAdapter(MainContent.ITEMS, listener)
                addItemDecoration(DividerItemDecoration(context,
                        LinearLayoutManager(context).orientation))
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is OnItemClickListener<*>) {
            listener = activity as MainActivity
        } else {
            throw RuntimeException("$context must implement OnItemClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                MainFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
