package com.tustar.filemanager.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.filemanager.model.CachingDocumentFile
import com.tustar.filemanager.utils.FileUtils
import com.tustar.rxjava.R
import com.tustar.rxjava.base.OnItemClickListener
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.fragment_detail.*

private const val ARG_DIRECTORY_URI = "arg_directory_uri"

class DetailFragment : Fragment(), OnItemClickListener<CachingDocumentFile> {

    private lateinit var directoryUri: Uri
    private lateinit var viewModel: DetailViewModel
    private lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryUri = arguments?.getString(ARG_DIRECTORY_URI)?.toUri()
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        viewModel = ViewModelProviders.of(this)
                .get(DetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadDirectory(directoryUri)
    }

    override fun onItemClick(clickedDocument: CachingDocumentFile) {
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

    private fun initObservers() {
        viewModel.documents.observe(this, Observer { documents ->
            documents?.let { detailAdapter.setEntries(documents) }
        })

        viewModel.openDirectory.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { directory ->
                (activity as? DetailActivity)?.showDirectoryContents(directory.uri)
            }
        })

        viewModel.openDocument.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                FileUtils.openDocument(context, document)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(directoryUri: Uri) = DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_DIRECTORY_URI, directoryUri.toString())
            }
        }
    }
}
