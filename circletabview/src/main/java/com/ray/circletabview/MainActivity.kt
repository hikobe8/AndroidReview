package com.ray.circletabview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_1.setVisibility(false)
        btn_2.setVisibility(false)
        btn_3.setVisibility(false)
        circle.visibilityCallback = object : CircleView.VisiblityCallback{
            override fun onOpen() {
                btn_1.setVisibility(true)
                btn_2.setVisibility(true)
                btn_3.setVisibility(true)
            }

            override fun onClosed() {
                btn_1.setVisibility(false)
                btn_2.setVisibility(false)
                btn_3.setVisibility(false)
            }

        }
        iv_tab.setOnClickListener {
            circle.toggle()
        }
    }
}

fun View.setVisibility(visible:Boolean = false){
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
    isEnabled = visible
    isClickable = visible
}
