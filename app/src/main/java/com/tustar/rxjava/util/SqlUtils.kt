package com.tustar.rxjava.util

/**
 * Created by tustar on 16-8-17.
 */
object SqlUtils {


    fun sqlEscapeString(keyWord: String): String? {
        var keyWord = keyWord

        if (keyWord.isBlank()) {
            return null
        }

        if (keyWord.contains("'")) {
            keyWord = keyWord.replace("'", "''")
        }

        return keyWord
    }

    fun sqlEscapeLikeString(keyWord: String?): String? {
        var keyWord = keyWord

        if (keyWord.isNullOrEmpty()) {
            return null
        }

        if (containsSpecialChar(keyWord)) {
            keyWord = escape(keyWord)
        }

        return keyWord
    }

    fun escape(keyWord: String?): String? {
        var keyWord = keyWord
        if (keyWord.isNullOrEmpty()) {
            return null
        }

        keyWord = keyWord!!.replace("/", "//")
        keyWord = keyWord.replace("'", "''")
        keyWord = keyWord.replace("[", "/[")
        keyWord = keyWord.replace("]", "/]")
        keyWord = keyWord.replace("%", "/%")
        keyWord = keyWord.replace("&", "/&")
        keyWord = keyWord.replace("_", "/_")
        keyWord = keyWord.replace("(", "/(")
        keyWord = keyWord.replace(")", "/)")

        return keyWord
    }

    fun containsSpecialChar(keyWord: String?): Boolean {

        if (keyWord.isNullOrEmpty()) {
            return false
        }

        if (keyWord!!.contains("/")) {
            return true
        }

        if (keyWord.contains("'")) {
            return true
        }

        if (keyWord.contains("[")) {
            return true
        }

        if (keyWord.contains("]")) {
            return true
        }

        if (keyWord.contains("%")) {
            return true
        }

        if (keyWord.contains("&")) {
            return true
        }

        if (keyWord.contains("_")) {
            return true
        }

        if (keyWord.contains("(")) {
            return true
        }

        return keyWord.contains(")")

    }
}
