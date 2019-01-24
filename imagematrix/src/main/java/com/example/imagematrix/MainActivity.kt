package com.example.imagematrix

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv.viewTreeObserver.addOnGlobalLayoutListener {
            if (iv.height > 0) {
                val viewWidth = iv.width
                val viewHeight = iv.height
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg)
                val result = Bitmap.createBitmap(iv.width, iv.height, Bitmap.Config.ARGB_4444)
                val canvas = Canvas(result)
                val scale = Math.max(viewWidth.toDouble()/bitmap.width, viewHeight.toDouble()/bitmap.height)
                val mt = Matrix().apply {
                    postTranslate(iv.width /2f- bitmap.width/2f, iv.height/2f - bitmap.height/2f)
                    postScale(scale.toFloat(), scale.toFloat(),result.width/2f, result.height/2f)
                }
                canvas.drawBitmap(bitmap, mt, null)
                iv.scaleType = ImageView.ScaleType.MATRIX
                iv.setImageBitmap(result)
            }
        }
    }
}
