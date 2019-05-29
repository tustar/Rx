package com.tustar.filemanager.ui.volume

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tustar.filemanager.model.VolumeItem

class VolumeViewModel(application: Application) : AndroidViewModel(application) {

    private val _volumes = MutableLiveData<List<VolumeItem>>()
    val volumes: LiveData<List<VolumeItem>>
        get() = _volumes

    fun getVolumes() {
        _volumes.postValue(VolumeItem.getVolumeItems(getApplication()))
    }
}