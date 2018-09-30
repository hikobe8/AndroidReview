package com.ray.opengl.camera.sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-30 11:48
 *  description : 
 */
public class FaceRectView extends View {

    private Paint mPaint;
    private Rect mRect = new Rect();

    public FaceRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(16);
        mPaint.setColor(Color.RED);
    }

    public void setRect(Rect rect){
        mRect.top = (int) (rect.top/0.25f);
        mRect.left = (int) (rect.left/0.25f);
        mRect.right = (int) (rect.right/0.25f);
        mRect.bottom = (int) (rect.bottom/0.25f);
//        mRect = rect;
        postInvalidate();
    }

    public void
    setRect(Rect rect, float scale){
        mRect.top = (int) (rect.top/scale);
        mRect.left = (int) (rect.left/scale);
        mRect.right = (int) (rect.right/scale);
        mRect.bottom = (int) (rect.bottom/scale);
//        mRect = rect;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mRect, mPaint);
    }
}
