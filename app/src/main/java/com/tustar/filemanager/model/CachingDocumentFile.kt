package com.tustar.filemanager.model

import androidx.documentfile.provider.DocumentFile

data class CachingDocumentFile(private val documentFile: DocumentFile) {
    val name: String? by lazy { documentFile.name }
    val type: String? by lazy { documentFile.type }
    val isDirectory: Boolean by lazy { documentFile.isDirectory }
    val isHidden: Boolean? by lazy { documentFile.name?.startsWith(".") }
    val lastModified: Long by lazy { documentFile.lastModified() }
    val length: Long by lazy {
        if (documentFile.isDirectory) {
            documentFile.listFiles().filterNot {
                it.name?.startsWith(".") ?: false
            }.size.toLong()
        } else {
            documentFile.length()
        }
    }

    val uri get() = documentFile.uri

    fun rename(newName: String): CachingDocumentFile {
        documentFile.renameTo(newName)
        return CachingDocumentFile(documentFile)
    }
}

fun Array<DocumentFile>.toCachingList(): List<CachingDocumentFile> {
    val list = mutableListOf<CachingDocumentFile>()
    forEach { list += CachingDocumentFile(it) }
    return list
}