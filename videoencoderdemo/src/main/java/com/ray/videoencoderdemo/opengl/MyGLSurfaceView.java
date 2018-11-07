package com.ray.videoencoderdemo.opengl;

import android.content.Context;
import android.util.AttributeSet;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-07 15:36
 *  description : 
 */
public class MyGLSurfaceView extends RayEGLSurfaceView {
    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRenderer(new MyRenderer());
        setRenderMode(RayEGLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
