package com.tustar.filemanager.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.ImageBucketFileItem
import com.tustar.filemanager.model.ImageFileItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ImageBucketItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<ImageFileItem>>()
    val documents = _documents

    fun loadImageByBucketId(bucketId:Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryImagesByeBucketId(bucketId)
            }.let {
                _documents.postValue(it)
            }
        }
    }
}