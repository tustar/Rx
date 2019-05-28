package com.tustar.filemanager.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tustar.rxjava.R

data class CategoryItem(@DrawableRes val icon: Int,
                        @StringRes val name: Int,
                        var count: Int = 0,
                        var hasNew: Boolean = false) {
    companion object {
        private val IMAGE = CategoryItem(
                icon = R.drawable.format_image,
                name = R.string.category_image
        )
        private val AUDIO = CategoryItem(
                icon = R.drawable.format_audio,
                name = R.string.category_audio
        )
        private val VIDEO = CategoryItem(
                icon = R.drawable.format_video,
                name = R.string.category_video
        )
        private val DOC = CategoryItem(
                icon = R.drawable.format_doc,
                name = R.string.category_doc
        )
        private val APP = CategoryItem(
                icon = R.drawable.format_app,
                name = R.string.category_app
        )
        private val ARCHIVES = CategoryItem(
                icon = R.drawable.format_archives,
                name = R.string.category_archives
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