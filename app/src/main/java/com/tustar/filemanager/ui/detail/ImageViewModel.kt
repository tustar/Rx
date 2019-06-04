package com.tustar.filemanager.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.ImageFileItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ImageViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<ImageFileItem>>()
    val documents = _documents

    fun loadImageBucket() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryImageBuckets()
            }.let {
                _documents.postValue(it)
            }
        }
    }

    fun loadImageByBucketId(bucketId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryImagesByBucketId(bucketId)
            }.let {
                _documents.postValue(it)
            }
        }
    }
}