package com.tustar.filemanager.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import androidx.fragment.app.Fragment
import com.tustar.rxjava.util.Logger


open class BaseStorageFragment : Fragment() {

    private val mTFCardReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let {
                Logger.i("action: $it")
                onStorageStateChanged()
            }
        }
    }
    private val mOtgReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Logger.i("action:${intent.action}")
            if (intent.action == ACTION_VOLUME_STATE_CHANGED) {
                when (val state = intent.getIntExtra(EXTRA_VOLUME_STATE, STATE_UNMOUNTED)) {
                    STATE_UNMOUNTED -> Logger.d(" STATE_UNMOUNTED: $state")
                    STATE_CHECKING -> Logger.d(" STATE_CHECKING: $state")
                    STATE_MOUNTED -> Logger.d(" STATE_MOUNTED: $state")
                    STATE_MOUNTED_READ_ONLY -> Logger.d(" STATE_MOUNTED_READ_ONLY: $state")
                    STATE_FORMATTING -> Logger.d(" STATE_FORMATTING: $state")
                    STATE_EJECTING -> Logger.d(" STATE_EJECTING: $state")
                    STATE_UNMOUNTABLE -> Logger.d(" STATE_UNMOUNTABLE: $state")
                    STATE_REMOVED -> Logger.d(" STATE_REMOVED: $state")
                    STATE_BAD_REMOVAL -> Logger.d(" STATE_BAD_REMOVAL: $state")
                }
            }

            onStorageStateChanged()
        }
    }

    override fun onResume() {
        super.onResume()

        registerTFCardReceiver()
        registerOtgReceiver()
    }

    override fun onPause() {
        super.onPause()

        unregisterTFCardReceiver()
        unregisterOtgReceiver()
    }

    private fun registerTFCardReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_EJECT)
        filter.addAction(Intent.ACTION_MEDIA_REMOVED)
        filter.addDataScheme("file")
        activity!!.registerReceiver(mTFCardReceiver, filter)
    }

    private fun unregisterTFCardReceiver() {
        if (mTFCardReceiver == null) {
            return
        }

        activity!!.unregisterReceiver(mTFCardReceiver)
    }

    private fun registerOtgReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        intentFilter.addAction(ACTION_VOLUME_STATE_CHANGED)
        activity!!.registerReceiver(mOtgReceiver, intentFilter)
    }

    private fun unregisterOtgReceiver() {
        if (mOtgReceiver == null) {
            return
        }

        activity!!.unregisterReceiver(mOtgReceiver)
    }

    open fun onStorageStateChanged() {
        Logger.i()
    }

    companion object {
        const val ACTION_VOLUME_STATE_CHANGED = "android.os.storage.action.VOLUME_STATE_CHANGED"
        const val EXTRA_VOLUME_STATE = "android.os.storage.extra.VOLUME_STATE"

        //
        const val STATE_UNMOUNTED = 0
        const val STATE_CHECKING = 1
        const val STATE_MOUNTED = 2
        const val STATE_MOUNTED_READ_ONLY = 3
        const val STATE_FORMATTING = 4
        const val STATE_EJECTING = 5
        const val STATE_UNMOUNTABLE = 6
        const val STATE_REMOVED = 7
        const val STATE_BAD_REMOVAL = 8
    }
}