package com.ray.customizedview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ray.customizedview.path.PathActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickPath(view: View) {
        PathActivity.launchActivty(this)
    }

}
