package com.tustar.filemanager.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tustar.filemanager.annotation.*
import com.tustar.rxjava.R

data class CategoryItem(@DrawableRes val icon: Int,
                        @StringRes val name: Int,
                        var count: Int = 0,
                        var hasNew: Boolean = false,
                       @CategoryType val type: Int) {
    companion object {
        private val IMAGE = CategoryItem(
                icon = R.drawable.format_image,
                name = R.string.category_image,
                type = TYPE_IMAGE
        )
        private val AUDIO = CategoryItem(
                icon = R.drawable.format_audio,
                name = R.string.category_audio,
                type = TYPE_AUDIO
        )
        private val VIDEO = CategoryItem(
                icon = R.drawable.format_video,
                name = R.string.category_video,
                type = TYPE_VIDEO
        )
        private val DOC = CategoryItem(
                icon = R.drawable.format_doc,
                name = R.string.category_doc,
                type = TYPE_DOC
        )
        private val APP = CategoryItem(
                icon = R.drawable.format_app,
                name = R.string.category_app,
                type = TYPE_APP
        )
        private val ARCHIVES = CategoryItem(
                icon = R.drawable.format_archives,
                name = R.string.category_archives,
                type = TYPE_ARCHIVES
        )

        fun getCategoryItems(): List<CategoryItem> = listOf(
                IMAGE,
                AUDIO,
                VIDEO,
                DOC,
                APP,
                ARCHIVES)
    }
}