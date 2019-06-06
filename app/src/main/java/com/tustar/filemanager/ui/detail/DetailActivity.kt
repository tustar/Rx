package com.tustar.filemanager.ui.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.transaction
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger


class DetailActivity : AppCompatActivity() {

    private lateinit var params: DetailParams
    private lateinit var detailFragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        //
        params = intent.getParcelableExtra(ARG_DETAIL_PARAMS)
                ?: throw IllegalArgumentException("Must pass URI of directory to open")
        params.name?.let{
            title = it
        }

        supportFragmentManager.transaction {
            val tag = params.type.toString()
            detailFragment = DetailFactory.create(params)
            replace(R.id.detail_fragment_container, detailFragment, tag)
        }
    }

    override fun onBackPressed() {
        if(detailFragment.onBackPressed()) {
            super.onBackPressed()
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
