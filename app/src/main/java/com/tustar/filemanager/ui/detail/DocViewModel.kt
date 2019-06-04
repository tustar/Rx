package com.tustar.filemanager.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.MediaItem
import com.tustar.filemanager.model.VideoItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DocViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<MediaItem>>()
    val documents = _documents

    fun loadDocs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryDocs()
            }.let {
                _documents.postValue(it)
            }
        }
    }
}