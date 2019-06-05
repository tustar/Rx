package com.tustar.filemanager.model

import android.database.Cursor
import android.provider.DocumentsContract
import com.tustar.filemanager.extension.getCursorInt
import com.tustar.filemanager.extension.getCursorLong
import com.tustar.filemanager.extension.getCursorString

class RootInfo {
    var rootId: String? = null
    var flags: Int = 0
    var icon: Int = 0
    var title: String? = null
    var summary: String? = null
    var documentId: String? = null
    var availableBytes: Long = 0
    var capacityBytes: Long = 0
    var mimeTypes: String? = null

    companion object {
        fun fromRootsCursor(cursor: Cursor): RootInfo {
            val root = RootInfo()
            cursor.apply {
                root.rootId = getCursorString(DocumentsContract.Root.COLUMN_ROOT_ID)
                root.flags = getCursorInt(DocumentsContract.Root.COLUMN_FLAGS)
                root.icon = getCursorInt(DocumentsContract.Root.COLUMN_ICON)
                root.title = getCursorString(DocumentsContract.Root.COLUMN_TITLE)
                root.summary = getCursorString(DocumentsContract.Root.COLUMN_SUMMARY)
                root.documentId = getCursorString(DocumentsContract.Root.COLUMN_DOCUMENT_ID)
                root.availableBytes = getCursorLong(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES)
                root.capacityBytes = getCursorLong(DocumentsContract.Root.COLUMN_CAPACITY_BYTES)
                root.mimeTypes = getCursorString(DocumentsContract.Root.COLUMN_MIME_TYPES)
            }

            return root
        }
    }
}