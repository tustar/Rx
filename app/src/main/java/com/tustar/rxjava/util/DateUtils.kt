package com.tustar.rxjava.util

import java.text.SimpleDateFormat
import java.util.*


object DateUtils {

    private const val DATE_FORMAT = "yyyy-M-d HH:mm"

    @JvmStatic
    fun millisToUTCDate(millis: Long): String {
        val date = Date(millis)
        val df = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        df.timeZone = TimeZone.getDefault()
        return df.format(date)
    }
}