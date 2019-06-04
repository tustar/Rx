package com.tustar.filemanager.utils

object SizeUtils {

    const val SIZE_STEP = 1024L
    const val B = SIZE_STEP
    const val KB = SIZE_STEP * B
    const val MB = SIZE_STEP * KB
    const val GB = SIZE_STEP * MB
    const val TB = SIZE_STEP * GB


    fun formatFileSize(size: Long, decimal: Int = 1): String {
        val format = "%." + decimal + "f"
        val value: Double
        var unit: String = ""
        when (size) {
            in 0 until B -> {
                return "${size}B"
            }
            in B until KB -> {
                unit = "KB"
                value = size.toDouble() / B
            }
            in KB until MB -> {
                unit = "MB"
                value = size.toDouble() / KB
            }
            in MB until GB -> {
                unit = "GB"
                value = size.toDouble() / MB
            }
            else -> {
                unit = "TB"
                value = size.toDouble() / GB
            }
        }

        return if (value >= 100) {
            "$value$unit"
        } else {
            String.format(format, value) + unit
        }
    }
}