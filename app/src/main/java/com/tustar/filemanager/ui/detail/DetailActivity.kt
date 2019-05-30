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
import com.tustar.filemanager.annotation.*
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger


class DetailActivity : AppCompatActivity() {

    private var directoryUri: Uri? = null
    @CategoryType
    private var detailType: Int = TYPE_STORAGE_PHONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        title = getString(R.string.title_activity_files)
        //
        checkPermissionRequest(this)
        //
        detailType = intent.getIntExtra(DETAIL_STORAGE_TYPE, TYPE_STORAGE_PHONE)

        loadData()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return false
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

    private fun loadData() {
        when (detailType) {
            TYPE_STORAGE_PHONE, TYPE_STORAGE_SDCARD, TYPE_STORAGE_USB -> {
                directoryUri = intent.getParcelableExtra(DETAIL_STORAGE_URI) as Uri
                showDetailContent(directoryUri)
            }
            else -> showDetailContent()
        }
    }

    fun showDetailContent(directoryUri: Uri? = null, bucketId: Long = 0) {
        if (bucketId != 0L) {
            detailType = TYPE_IMAGE_BUCKET_ITEM
        }
        supportFragmentManager.transaction {
            val tag = detailType.toString()
            val detailFragment = DetailFactory.create(detailType, directoryUri, bucketId)
            replace(R.id.detail_fragment_container, detailFragment, tag)
            addToBackStack(tag)
        }
    }


    companion object {
        const val DETAIL_STORAGE_URI = "detail_storage_uri"
        const val DETAIL_STORAGE_TYPE = "detail_storage_type"

        fun openVolumeDetail(context: Context?, @CategoryType detailType: Int, directoryUri: Uri) {
            context?.let {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DETAIL_STORAGE_URI, directoryUri)
                    putExtra(DETAIL_STORAGE_TYPE, detailType)
                }
                it.startActivity(intent)
            }
        }

        fun openCategoryDetail(context: Context?, @CategoryType detailType: Int) {
            context?.let {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DETAIL_STORAGE_TYPE, detailType)
                }
                it.startActivity(intent)
            }
        }
    }
}
