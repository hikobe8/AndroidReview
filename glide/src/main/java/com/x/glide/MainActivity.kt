package com.x.glide

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fengj)
        val roundCorner = ImageUtils.toRoundCorner(bitmap, 100, ImageUtils.CORNER_TOP_LEFT or ImageUtils.CORNER_TOP_RIGHT)
        image.setImageBitmap(roundCorner)
    }
}
