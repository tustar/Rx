package com.tustar.filemanager.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.CategoryItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MediaStoreRepository.get(application)

    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>>
        get() = _categories

    private val _imageCount = MutableLiveData<Int>()
    val imageCount: LiveData<Int>
        get() = _imageCount

    private val _audioCount = MutableLiveData<Int>()
    val audioCount: LiveData<Int>
        get() = _audioCount

    private val _videoCount = MutableLiveData<Int>()
    val videoCount: LiveData<Int>
        get() = _videoCount

    private val _docCount = MutableLiveData<Int>()
    val docCount: LiveData<Int>
        get() = _docCount

    private val _appCount = MutableLiveData<Int>()
    val appCount: LiveData<Int>
        get() = _appCount

    private val _archivesCount = MutableLiveData<Int>()
    val archivesCount: LiveData<Int>
        get() = _archivesCount

    fun loadCategories() {
        _categories.postValue(CategoryItem.getCategoryItems())
    }

    fun loadImageBucketCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryImageBucketCount()
            }?.let {
                _imageCount.postValue(it)
            }
        }
    }

    fun loadAudioCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryAudioCount()
            }?.let {
                _audioCount.postValue(it)
            }
        }
    }

    fun loadVideoCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryVideoCount()
            }?.let {
                _videoCount.postValue(it)
            }
        }
    }

    fun loadDocCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryDocCount()
            }?.let {
                _docCount.postValue(it)
            }
        }
    }

    fun loadAppCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryAppCount()
            }?.let {
                _appCount.postValue(it)
            }
        }
    }

    fun loadArchivesCount() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryArchivesCount()
            }?.let {
                _archivesCount.postValue(it)
            }
        }
    }
}