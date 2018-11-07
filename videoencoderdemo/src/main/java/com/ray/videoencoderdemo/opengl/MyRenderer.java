package com.ray.videoencoderdemo.opengl;

import android.opengl.GLES20;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-07 15:37
 *  description : 
 */
public class MyRenderer implements RayEGLSurfaceView.RayGLRenderer {
    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.f, 1.f, 0f, 1f);
    }
}
