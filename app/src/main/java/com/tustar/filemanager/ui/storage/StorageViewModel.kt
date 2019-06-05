package com.tustar.filemanager.ui.storage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.StorageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StorageViewModel(application: Application) : AndroidViewModel(application) {

    private val _storages = MutableLiveData<List<StorageItem>>()
    val storages: LiveData<List<StorageItem>>
        get() = _storages

    fun getStorages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                StorageItem.getStorageItems(getApplication())
            }.let {
                _storages.postValue(it)
            }
        }
    }
}