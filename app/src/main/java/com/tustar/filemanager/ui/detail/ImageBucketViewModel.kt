package com.tustar.filemanager.ui.detail

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.LiveEvent
import com.tustar.filemanager.model.DetailFileItem
import com.tustar.filemanager.model.ImageBucketFileItem
import com.tustar.filemanager.model.VolumeFileItem
import com.tustar.filemanager.model.toList
import com.tustar.filemanager.repository.MediaStoreRepository
import com.tustar.rxjava.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ImageBucketViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<ImageBucketFileItem>>()
    val documents = _documents

    fun loadImageBucket() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryImageBucketItems()
            }.let {
                _documents.postValue(it)
            }
        }
    }
}