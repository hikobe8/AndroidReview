package com.ray.customizedview.scroll

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ray.customizedview.R

/**
 * Author : hikobe8@github.com
 * Time : 2018/12/17 11:56 PM
 * Description :
 */
class ScrollAViewByLayoutActivity : AppCompatActivity() {

    companion object {
        fun launchActivty(context: AppCompatActivity) {
            context.startActivity(Intent(context, ScrollAViewByLayoutActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_a_view_layout)
    }

}