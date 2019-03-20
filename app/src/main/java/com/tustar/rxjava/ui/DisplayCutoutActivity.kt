package com.tustar.rxjava.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tustar.rxjava.R
import kotlinx.android.synthetic.main.activity_display_cutout.*
import kotlinx.android.synthetic.main.activity_display_cutout.view.*

class DisplayCutoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_cutout)

        getNotchParams()
    }

    private fun getNotchParams() {
        display_cutout_text.text = ""
        window.decorView.apply {
            post {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val displayCutout = rootWindowInsets.displayCutout
                    display_cutout_text.append("安全区域距离屏幕的距离(left: ${displayCutout.safeInsetLeft},"
                            + "top: ${displayCutout.safeInsetTop}, "
                            + "right: ${displayCutout.safeInsetRight}, "
                            + "bottom: ${displayCutout.safeInsetBottom})\n")

                    val rects = displayCutout.boundingRects
                    if (rects.isNullOrEmpty()) {
                        display_cutout_text.append("不是刘海屏\n")
                    } else {
                        display_cutout_text.append("刘海的数量${rects.size}\n")
                        rects.forEachIndexed { index, rect ->
                            display_cutout_text.append("刘海${index + 1}区域:$rect\n")
                        }
                    }
                }
            }
        }
    }
}
