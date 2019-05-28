package com.tustar.filemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager

object WifiUtils {
    fun getWifiIp(context: Context): String? {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm?.connectionInfo?.let {
            return intToIp(it.ipAddress)
        }

        return null
    }

    private fun intToIp(i: Int): String {
        return "${(i and 0xFF)}.${(i shr 8 and 0xFF)}.${(i shr 16 and 0xFF)}.${(i shr 24 and 0xFF)}"
    }

    fun isWiFiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = cm.activeNetwork
            val capabilities = cm.getNetworkCapabilities(network)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
        } else {
            cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }
}