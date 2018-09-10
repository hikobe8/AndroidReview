package com.ray.customizedview.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.ray.customizedview.R

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-10 18:49
 *  description :
 */
class CommonShaderView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mShaderPaint = Paint()
    private var mTextPaint = Paint()
    private var mRadius = 0f
    private var mTextForDraw:String = "CLAMP"
    private var mTileMode = Shader.TileMode.CLAMP

    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.ShaderView, 0, 0)
        mTextForDraw = array?.getString(R.styleable.ShaderView_tile_mode)!!
        var type = array?.getInteger(R.styleable.PathView_fillType, 0)
        mTileMode = Shader.TileMode.valueOf(type)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = w/4f
        var shader = LinearGradient(mRadius,mRadius, mRadius*3, mRadius*3, Color.parseColor("#E91E63"),
                Color.parseColor("#2196F3"), Shader.TileMode.CLAMP )
        mShaderPaint.shader = shader
        mShaderPaint.isAntiAlias = true

        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18f, context.resources.displayMetrics)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawText(mTextForDraw, mRadius, mRadius - 20f, mTextPaint)
        canvas?.drawCircle(mRadius*2,mRadius*2, mRadius, mShaderPaint)
    }

}