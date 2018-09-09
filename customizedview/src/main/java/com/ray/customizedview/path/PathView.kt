package com.ray.customizedview.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ray.customizedview.R

/**
 * Author : hikobe8@github.com
 * Time : 2018/9/9 下午4:42
 * Description :
 */
class PathView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPaint: Paint = Paint()
    private var mPath: Path = Path()
    private var mRadius: Float = 0f
    private var mCCW = false

    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.PathView, 0, 0)
        mCCW = array?.getBoolean(R.styleable.PathView_direction, false)!!
        array.recycle()
        mPaint.color = Color.BLUE
        mPaint.isAntiAlias = true
//        mPaint.style = Paint.Style.STROKE
//        mPaint.strokeWidth = 20f
        mPath.fillType = Path.FillType.WINDING
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = w / 4f
        mPath.reset()
        mPath.addCircle(mRadius * 1.5f, h / 2f, mRadius, Path.Direction.CW)
        mPath.addCircle(mRadius * 2.5f, h / 2f, mRadius, if (mCCW) Path.Direction.CCW else Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mPaint)
    }

}