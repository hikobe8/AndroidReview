package com.ray.customizedview.shadow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Author : hikobe8@github.com
 * Time : 2018/9/22 下午11:36
 * Description :
 */
class ShadowText(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var mPaint = Paint()
    val mText = "Hello With Shadow"

    init {
        mPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24f, context?.resources?.displayMetrics)
        mPaint.color = Color.BLACK
        mPaint.setShadowLayer(10f, 0f,0f, Color.RED)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawText(mText, 0, mText.length, width/4f, height/2f, mPaint)
    }

}