package com.palmax.learntransition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun jump(v:View) {
        startActivity(Intent(this, SecondActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this, v, "image").toBundle())
    }


}
