package com.tustar.rxjava.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tustar.rxjava.MainActivity
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import kotlinx.android.synthetic.main.activity_notification_detail.*

class NotificationDetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail)
        showIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        Logger.i()
        super.onNewIntent(intent)
        showIntent(intent)
    }

    private fun showIntent(intent: Intent?) {
        intent?.let {
            val which = intent.getStringExtra(MainActivity.INTENT_KEY_FROM_WHICH)
            if (which.isNullOrEmpty()) {
                return
            }

            notification_detail_text.text = "Intent来自${which}通知"
        }
    }
}
