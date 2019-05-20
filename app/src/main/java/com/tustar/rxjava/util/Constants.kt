package com.tustar.rxjava.util

import android.os.Environment
import java.io.File

object Constants {

    const val PORT = 3000
    const val MAX_WIDTH = 480
    val DIR: File
        get() = Environment.getExternalStorageDirectory()

    object RxBusEventType {
        const val POPUP_MENU_DIALOG_SHOW_DISMISS = "POPUP MENU DIALOG SHOW " + "DISMISS"
        const val WIFI_CONNECT_CHANGE_EVENT = "WIFI CONNECT CHANGE EVENT"
        const val LOAD_FILE_LIST = "LOAD BOOK LIST"
    }
}
