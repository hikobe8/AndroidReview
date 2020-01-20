package com.hikobe8.view.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Author : Ray
 * Time : 2020-01-20 11:44
 * Description : 饼状图
 */
class PieChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        var RADIUS = Util.dp2px(150f)
        val PIE_ANGLES = arrayOf(70f, 100f, 50f, 80f, 60f)
        val PIE_COLORS = arrayOf(Color.RED, Color.parseColor("#123456"), Color.GREEN, Color.MAGENTA, Color.DKGRAY)
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBound = RectF()

    private var mOutlineIndex = 1 //当前突出显示的块

    private var mOutlineOffset = Util.dp2px(10f) //突出显示的间隔距离

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBound.set(w / 2f - RADIUS, h / 2f - RADIUS, w / 2f + RADIUS, h / 2f + RADIUS)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            var currentAngle = 0f
            PIE_ANGLES.forEachIndexed { index, _ ->
                if (index == mOutlineIndex) {
                    save()
                    translate(
                            (mOutlineOffset * cos(Math.toRadians((currentAngle + PIE_ANGLES[index] / 2f).toDouble()))).toFloat(),
                            (mOutlineOffset * sin(Math.toRadians(((currentAngle + PIE_ANGLES[index] / 2f).toDouble())))).toFloat()
                    )
                }
                mPaint.color = PIE_COLORS[index]
                drawArc(mBound, currentAngle, PIE_ANGLES[index], true, mPaint)
                currentAngle += PIE_ANGLES[index]
                if (index == mOutlineIndex) {
                    restore()
                }
            }
        }
    }

}