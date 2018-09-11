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
    private var mTextForDraw = ""
    private var mTileMode = Shader.TileMode.CLAMP
    private var mTextStartX = 0f
    private lateinit var mRectF: RectF
    private var mTextBaseLine = 0f

    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.CommonShaderView, 0, 0)
        val type = array?.getInteger(R.styleable.CommonShaderView_tile_mode, 0)
        when (type) {
            Shader.TileMode.CLAMP.ordinal -> mTileMode =  Shader.TileMode.CLAMP
            Shader.TileMode.REPEAT.ordinal -> mTileMode = Shader.TileMode.REPEAT
            Shader.TileMode.MIRROR.ordinal -> mTileMode = Shader.TileMode.MIRROR
        }
        mTextForDraw = "Shader.TileMode." + mTileMode.name
        array?.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(size, size/2)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val halfRectWidth = w/8f
        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18f, context.resources.displayMetrics)
        val textBounds = Rect()
        mTextPaint.getTextBounds(mTextForDraw, 0, mTextForDraw.length, textBounds)
        val textWidth = textBounds.width()
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        mTextBaseLine = textHeight -fontMetrics.top
        mTextStartX = w/2f - textWidth/2f
        mRectF = RectF(0f, textHeight*3f, w*1f, h*1f)
        var shader = LinearGradient(halfRectWidth * 3f, textHeight*3f, halfRectWidth*5, textHeight*3f, Color.parseColor("#E91E63"),
                Color.parseColor("#2196F3"), mTileMode )
        mShaderPaint.shader = shader
        mShaderPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawText(mTextForDraw, mTextStartX, mTextBaseLine, mTextPaint)
        canvas?.drawRect(mRectF, mShaderPaint)
    }

}