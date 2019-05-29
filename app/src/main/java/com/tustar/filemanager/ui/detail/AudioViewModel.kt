package com.tustar.filemanager.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tustar.filemanager.model.AudioFileItem
import com.tustar.filemanager.repository.MediaStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AudioViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = MediaStoreRepository.get(application)

    private val _documents = MutableLiveData<List<AudioFileItem>>()
    val documents = _documents

    fun loadAudios() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.queryAudios()
            }.let {
                _documents.postValue(it)
            }
        }
    }
}