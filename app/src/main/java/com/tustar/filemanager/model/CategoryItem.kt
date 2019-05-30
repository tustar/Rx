package com.tustar.filemanager.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tustar.filemanager.annotation.*
import com.tustar.rxjava.R

data class CategoryItem(@DrawableRes val icon: Int,
                        @StringRes val name: Int,
                        var count: Int = 0,
                        var hasNew: Boolean = false,
                       @CategoryType val detailType: Int) {
    companion object {
        private val IMAGE = CategoryItem(
                icon = R.drawable.format_image,
                name = R.string.category_image,
                detailType = TYPE_IMAGE_BUCKET
        )
        private val AUDIO = CategoryItem(
                icon = R.drawable.format_audio,
                name = R.string.category_audio,
                detailType = TYPE_AUDIO
        )
        private val VIDEO = CategoryItem(
                icon = R.drawable.format_video,
                name = R.string.category_video,
                detailType = TYPE_VIDEO
        )
        private val DOC = CategoryItem(
                icon = R.drawable.format_doc,
                name = R.string.category_doc,
                detailType = TYPE_DOC
        )
        private val APP = CategoryItem(
                icon = R.drawable.format_app,
                name = R.string.category_app,
                detailType = TYPE_APP
        )
        private val ARCHIVES = CategoryItem(
                icon = R.drawable.format_archives,
                name = R.string.category_archives,
                detailType = TYPE_ARCHIVES
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