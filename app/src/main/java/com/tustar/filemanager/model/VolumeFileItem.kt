package com.tustar.filemanager.model

import androidx.documentfile.provider.DocumentFile

data class VolumeFileItem(private val documentFile: DocumentFile) : DetailFileItem() {
    override val name: String? by lazy { documentFile.name }
    override val type: String? by lazy { documentFile.type }
    override val isDirectory: Boolean by lazy { documentFile.isDirectory }
    override val isHidden: Boolean by lazy { documentFile.name?.startsWith(".") ?: false }
    override val lastModified: Long by lazy { documentFile.lastModified() }
    override val length: Long by lazy {
        if (documentFile.isDirectory) {
            documentFile.listFiles().filterNot {
                it.name?.startsWith(".") ?: false
            }.size.toLong()
        } else {
            documentFile.length()
        }
    }
    override val uri get() = documentFile.uri


    fun rename(newName: String): VolumeFileItem {
        documentFile.renameTo(newName)
        return VolumeFileItem(documentFile)
    }
}

fun Array<DocumentFile>.toList(): List<VolumeFileItem> {
    val list = mutableListOf<VolumeFileItem>()
    forEach { list += VolumeFileItem(it) }
    return list
}