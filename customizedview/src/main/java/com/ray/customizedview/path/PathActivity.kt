package com.ray.customizedview.path

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ray.customizedview.R

class PathActivity : AppCompatActivity() {

    companion object {

        fun launchActivty(context:AppCompatActivity){
            context.startActivity(Intent(context, PathActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)
    }
}
