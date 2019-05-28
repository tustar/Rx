package com.tustar.rxjava.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.tustar.rxjava.RxMainActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent == null) {
            return
        }

        when (intent.action) {
            RxMainActivity.ACTION_CLOSE_NOTIFICATION -> {
                val id = intent.getIntExtra(RxMainActivity.INTENT_KEY_NOTIFICATION_ID,
                        -1)
                if (id != -1) {
                    Toast.makeText(context, "取消${id}的通知", Toast.LENGTH_LONG).show()
                    NotificationManagerCompat.from(context).cancel(id)
                }
            }
            else -> {

            }
        }
    }
}
