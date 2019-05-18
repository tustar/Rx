package com.tustar.rxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.tustar.rxjava.ui.main.MainContent
import com.tustar.rxjava.ui.main.MainFragment
import com.tustar.rxjava.util.Logger


class MainActivity : AppCompatActivity(), MainFragment.OnItemClickListener {

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp() = mNavController.navigateUp()

    override fun onItemClick(item: MainContent.DummyItem) {
        mNavController.navigate(item.action)
        title = item.content
    }

    override fun onResume() {
        super.onResume()
        Logger.i()
    }

    override fun onPause() {
        super.onPause()
        Logger.i()
    }

    override fun onStop() {
        super.onStop()
        Logger.i()
    }

    companion object {
        const val INTENT_KEY_FROM_WHICH = "intent_key_from_which"
        const val INTENT_KEY_NOTIFICATION_ID = "intent_key_notification_id"
        const val ACTION_CLOSE_NOTIFICATION = "com.tustar.rxjava.ACTION_CLOSE_NOTIFICATION"
    }


}
