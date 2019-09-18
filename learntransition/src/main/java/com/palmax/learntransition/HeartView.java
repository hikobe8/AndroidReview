package com.palmax.learntransition;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2019-01-14 15:44
 *  description : Palmax三周年年会心形祝福动画
 */
public class HeartView extends View {
    public HeartView(Context context) {
        super(context);
    }

    private Paint paint;
    private ValueAnimator av;
    private float mLength;
    private PathMeasure pm;
    private AnimationListener mAnimationListener;

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setAnimationListener(AnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    interface AnimationListener{

        void onProgressUpdated(float fraction);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w < h)
            return;
        Path path = new Path();
        path.moveTo(w /2f, h /4f);
        path.cubicTo((w *5f)/7, h /16f,(w *12f)/13,(h *2f)/5, w /2f,(h *10f)/12);

        path.cubicTo(w / 13f, (h * 2f) / 5,w * 2 / 7f, h / 16f, w /2f, h /4f);

        pm =new PathMeasure(path,true);

        av = ValueAnimator.ofFloat(0,pm.getLength());
        av.setDuration(5000);
        av.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mAnimationListener != null) {
                    mAnimationListener.onProgressUpdated(animation.getAnimatedFraction());
                }
                mLength= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        av.start();
    }

    private void init() {
        paint =new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Path mPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        pm.getSegment(0,mLength, mPath,true);
        canvas.drawPath(mPath,paint);
        canvas.restore();
    }

    public void start() {
        av.start();
    }
}
