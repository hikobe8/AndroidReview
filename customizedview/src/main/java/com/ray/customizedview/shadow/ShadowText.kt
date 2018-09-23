package com.ray.customizedview.shadow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.ray.customizedview.R

/**
 * Author : hikobe8@github.com
 * Time : 2018/9/22 下午11:36
 * Description :
 */
class ShadowText(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPaint = Paint()
    private var mText = ""

    private var mBaseLine: Float

    private var mShadowOffsetX: Float

    private var mShadowOffsetY: Float


    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.ShadowText)
        mText = array?.getString(R.styleable.ShadowText_text)!!
        val textColor = array.getColor(R.styleable.ShadowText_textColor, Color.BLACK)
        val shadowColor = array.getColor(R.styleable.ShadowText_shadowColor, Color.BLACK)
        val textSize = array.getDimension(R.styleable.ShadowText_textSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, context?.resources?.displayMetrics))
        val shadowRadius = array.getFloat(R.styleable.ShadowText_shadowRadius, 10f)
        mShadowOffsetX = array.getFloat(R.styleable.ShadowText_shadowOffsetX, 0f)
        mShadowOffsetY = array.getFloat(R.styleable.ShadowText_shadowOffsetY, 0f)
        array.recycle()
        mPaint.isAntiAlias = true
        mPaint.textSize = textSize
        mPaint.color = textColor
        mPaint.setShadowLayer(shadowRadius, mShadowOffsetX,mShadowOffsetY, shadowColor)
        val fontMetrics = mPaint.fontMetrics
        mBaseLine = 0 + paddingTop - fontMetrics.top
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width:Int = (mPaint.measureText(mText) + 0.5f + paddingLeft + paddingRight + mShadowOffsetX).toInt()
        val height: Int = (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top + paddingTop + paddingBottom + mShadowOffsetY).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawText(mText,0f, mBaseLine, mPaint)
    }

}