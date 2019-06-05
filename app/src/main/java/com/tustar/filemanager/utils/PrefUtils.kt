package com.tustar.filemanager.utils

import android.content.Context
import android.net.Uri

object PrefUtils {

    private fun getSharedPreferences(context: Context) =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun saveUuidUri(context: Context, uuid: String, uri: Uri) {
        val preference = getSharedPreferences(context)
        preference.edit().putString(uuid, uri.toString()).apply()
    }

    fun getUuidUri(context: Context, uuid: String): String? {
        val preference = getSharedPreferences(context)
        return preference.getString(uuid, null)
    }
}