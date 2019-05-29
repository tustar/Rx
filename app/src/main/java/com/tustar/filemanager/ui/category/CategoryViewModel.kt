package com.tustar.filemanager.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tustar.filemanager.model.CategoryItem

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories = MutableLiveData<List<CategoryItem>>()
    val categories: LiveData<List<CategoryItem>>
        get() = _categories

    fun loadCategories() {
        _categories.postValue(CategoryItem.getCategoryItems())
    }
}