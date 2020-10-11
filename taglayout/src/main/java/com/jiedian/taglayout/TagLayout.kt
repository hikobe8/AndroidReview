package com.jiedian.taglayout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

/**
 * Author : Ray
 * Time : 2020/10/10 11:38 PM
 * Description :
 */
class TagLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val childBounds: ArrayList<Rect> = ArrayList()


    /**
     * 对所有的子view进行测量，然后测量自己的尺寸
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthUsed = 0
        var heightUsed = 0
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var lineHeight = 0
        var lineWidthUsed = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            //判断是否超过布局宽度，超过要换行
            if (widthMode != MeasureSpec.UNSPECIFIED
                    && child.measuredWidth + lineWidthUsed > widthSize) {
                //换行
                heightUsed += lineHeight
                lineWidthUsed = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }
            child.apply {
                if (childBounds.size <= i) {
                    childBounds.add(Rect())
                }
                childBounds[i].set(
                        lineWidthUsed, heightUsed, lineWidthUsed + measuredWidth, heightUsed + measuredHeight
                )
                lineWidthUsed += measuredWidth
                widthUsed = max(lineWidthUsed, widthUsed)
                lineHeight = max(lineHeight, measuredHeight)
            }
        }
        val measuredHeight = heightUsed + lineHeight
        setMeasuredDimension(widthUsed, measuredHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        childBounds.forEachIndexed { index, rect ->
            getChildAt(index).layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

}