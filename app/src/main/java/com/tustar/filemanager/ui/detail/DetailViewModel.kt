package com.tustar.filemanager.ui.detail

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.LiveEvent
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.VolumeFileItem
import com.tustar.filemanager.model.VolumeItem
import com.tustar.filemanager.model.toCachingList
import com.tustar.rxjava.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _documents = MutableLiveData<List<VolumeFileItem>>()
    val documents = _documents

    private val _openDirectory = MutableLiveData<LiveEvent<DetailFileItem>>()
    val openDirectory = _openDirectory

    private val _openDocument = MutableLiveData<LiveEvent<DetailFileItem>>()
    val openDocument = _openDocument

    fun loadDirectory(directoryUri: Uri) {
        Logger.d("directoryUri:$directoryUri")
        val documentsTree = DocumentFile.fromTreeUri(getApplication(), directoryUri) ?: return
        Logger.d("documentsTree:${documentsTree.uri}")
        val childDocuments = documentsTree.listFiles().toCachingList()

        // It's much nicer when the documents are sorted by something, so we'll sort the documents
        // we got by name. Unfortunate there may be quite a few documents, and sorting can take
        // some time, so we'll take advantage of coroutines to take this work off the main thread.
        viewModelScope.launch {
            val sortedDocuments = withContext(Dispatchers.IO) {
                childDocuments
                        .filterNot { it.isHidden ?: false }
                        .toMutableList().apply {
                            sortBy { it.name }
                        }

            }
            _documents.postValue(sortedDocuments)
        }
    }

    /**
     * Method to dispatch between clicking on a document (which should be opened), and
     * a directory (which the user wants to navigate into).
     */
    fun documentClicked(clickedDocument: DetailFileItem) {
        if (clickedDocument.isDirectory) {
            openDirectory.postValue(LiveEvent(clickedDocument))
        } else {
            openDocument.postValue(LiveEvent(clickedDocument))
        }
    }
}