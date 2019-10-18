package com.ray.customizedview.path

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ray.customizedview.R

class PathActivity : AppCompatActivity() {

    companion object {

        fun launchActivity(context:AppCompatActivity){
            context.startActivity(Intent(context, PathActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path)
    }
}
