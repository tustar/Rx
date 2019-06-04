package com.tustar.filemanager.ui.storage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tustar.filemanager.model.StorageItem

class StorageViewModel(application: Application) : AndroidViewModel(application) {

    private val _storages = MutableLiveData<List<StorageItem>>()
    val storages: LiveData<List<StorageItem>>
        get() = _storages

    fun getStorages() {
        _storages.postValue(StorageItem.getStorageItems(getApplication()))
    }
}