package com.tustar.rxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.tustar.rxjava.ui.MainFragment
import com.tustar.rxjava.ui.dummy.DummyContent

class MainActivity : AppCompatActivity(), MainFragment.OnItemClickListener {

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mNavController = findNavController(R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp() = mNavController.navigateUp()

    override fun onItemClick(item: DummyContent.DummyItem) {
        mNavController.navigate(item.action)
        title = item.content
    }
}
