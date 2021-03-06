package com.tustar.filemanager.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.VideoItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<VideoItem>>()
    val documents = _documents

    fun loadVideos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryVideos()
            }.let {
                _documents.postValue(it)
            }
        }
    }
}