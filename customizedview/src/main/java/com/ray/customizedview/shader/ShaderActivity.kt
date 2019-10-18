package com.ray.customizedview.shader

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ray.customizedview.R

class ShaderActivity : AppCompatActivity() {

    companion object {

        fun launchActivity(context:AppCompatActivity){
            context.startActivity(Intent(context, ShaderActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shader)
    }
}
