package com.tustar.rxjava.ui

import androidx.annotation.IdRes
import com.tustar.rxjava.R
import java.util.*

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object MainContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    private val ITEM_MAP: MutableMap<Int, DummyItem> = HashMap()

    init {
        addItem(DummyItem(1, "Observable", R.id.action_to_observable))
        addItem(DummyItem(2, "BackPressure", R.id.action_to_backpressure))
        addItem(DummyItem(3, "RxJava2+Retrofit2", R.id.action_to_retrofit))
        addItem(DummyItem(4, "OkHttp", R.id.action_to_okHttp))
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: Int,
                         val content: String,
                         @IdRes val action: Int) {
        override fun toString(): String = content
    }
}
