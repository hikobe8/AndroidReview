package com.ray.customizedview.shadow

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ray.customizedview.R

class ShadowTextActivity : AppCompatActivity() {

    companion object {
        fun launchActivity(context: AppCompatActivity) {
            context.startActivity(Intent(context, ShadowTextActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadow_text)
    }
}
