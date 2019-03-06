package com.tustar.rxjava

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.tustar.rxjava.ui.MainContent
import com.tustar.rxjava.ui.MainFragment
import com.tustar.rxjava.util.Logger


class MainActivity : AppCompatActivity(), MainFragment.OnItemClickListener {

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.nav_host_fragment)
        showIntent(intent)
    }

    override fun onSupportNavigateUp() = mNavController.navigateUp()

    override fun onItemClick(item: MainContent.DummyItem) {
        mNavController.navigate(item.action)
        title = item.content
    }

    override fun onNewIntent(intent: Intent?) {
        Logger.i()
        super.onNewIntent(intent)
        showIntent(intent)
    }

    private fun showIntent(intent: Intent?) {
        intent?.let {
            val which = intent.getStringExtra(INTENT_KEY_FROM_WHICH)
            if (which.isNullOrEmpty()) {
                return
            }
            Toast.makeText(this, "Intent来自${which}通知", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val INTENT_KEY_FROM_WHICH = "intent_key_from_which"
        const val INTENT_KEY_NOTIFICATION_ID = "intent_key_notification_id"
        const val ACTION_CLOSE_NOTIFICATION = "com.tustar.rxjava.ACTION_CLOSE_NOTIFICATION"
    }


}
