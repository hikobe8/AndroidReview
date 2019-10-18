package com.ray.customizedview.learn_styles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ray.customizedview.R

class LearnStylesActivity : AppCompatActivity() {

    companion object {
        fun launchActivity(context:AppCompatActivity){
            context.startActivity(Intent(context, LearnStylesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_styles)
    }
}
