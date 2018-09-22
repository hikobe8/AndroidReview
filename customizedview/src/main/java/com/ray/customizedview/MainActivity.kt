package com.ray.customizedview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ray.customizedview.path.PathActivity
import com.ray.customizedview.shader.ShaderActivity
import com.ray.customizedview.shadow.ShadowTextActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickPath(view: View) {
        PathActivity.launchActivity(this)
    }

    fun clickShader(view: View){
        ShaderActivity.launchActivity(this)
    }

    fun clickShadowText(view: View){
        ShadowTextActivity.launchActivty(this)
    }

}
