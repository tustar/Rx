package com.tustar.filemanager.ui.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger


class DetailActivity : AppCompatActivity() {

    private lateinit var params: DetailParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        title = getString(R.string.title_activity_files)
        //
        checkPermissionRequest(this)
        //
        params = intent.getParcelableExtra(ARG_DETAIL_PARAMS)
                ?: throw IllegalArgumentException("Must pass URI of directory to open")

        supportFragmentManager.transaction {
            val tag = params.type.toString()
            val detailFragment = DetailFactory.create(params)
            replace(R.id.detail_fragment_container, detailFragment, tag)
        }
    }

    private fun checkPermissionRequest(activity: FragmentActivity) {
        val permissions = RxPermissions(activity)
        permissions.setLogging(true)
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    Logger.d("granted = $granted")
                    if (granted) { // Always true pre-M
                        // I can control the camera now
                    } else {
                        // Oups permission denied
                    }
                }
    }

    companion object {
        const val ARG_DETAIL_PARAMS = "arg_detail_params"

        fun openVolumeDetail(context: Context?, params: DetailParams) {
            context?.let {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(ARG_DETAIL_PARAMS, params)
                }
                it.startActivity(intent)
            }
        }
    }
}
