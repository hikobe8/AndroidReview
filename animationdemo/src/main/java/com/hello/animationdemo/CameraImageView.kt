package com.hello.animationdemo

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Author : Ray
 * Time : 2020/8/11 6:12 PM
 * Description :
 */
class CameraImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBitmap: Bitmap = BitmapUtils.decodeBitmap(resources, R.drawable.head, 200f.dp2px().toInt())

    private val mBitmapWidth: Int
    private val mBitmapHeight: Int
    private val mCamera = Camera()

    private var rotationMiddle = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rotationTopCamera = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var rotationBottomCamera = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        mBitmapWidth = mBitmap.width
        mBitmapHeight = mBitmap.height
        mCamera.setLocation(0f, 0f, -4 * Resources.getSystem().displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {

            save()
            translate(width / 2f, height / 2f)
            rotate(-rotationMiddle)
            mCamera.save()
            mCamera.rotateX(rotationTopCamera)
            mCamera.applyToCanvas(this)
            mCamera.restore()
            clipRect(-mBitmapWidth, -mBitmapHeight, mBitmapWidth, 0)
            rotate(rotationMiddle)
            translate(-width / 2f, -height / 2f)
            drawBitmap(mBitmap, width / 2f - mBitmapWidth / 2f, height / 2f - mBitmapHeight / 2f, paint)
            restore()

            save()
            translate(width / 2f, height / 2f)
            rotate(-rotationMiddle)
            mCamera.save()
            mCamera.rotateX(rotationBottomCamera)
            mCamera.applyToCanvas(this)
            mCamera.restore()
            clipRect(-mBitmapWidth, 0, mBitmapWidth, mBitmapHeight)
            rotate(rotationMiddle)
            translate(-width / 2f, -height / 2f)
            drawBitmap(mBitmap, width / 2f - mBitmapWidth / 2f, height / 2f - mBitmapHeight / 2f, paint)
            restore()
        }
    }

}