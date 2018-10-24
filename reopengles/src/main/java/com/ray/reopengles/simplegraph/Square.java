package com.ray.reopengles.simplegraph;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-23 14:27
 *  description : 
 */
public class Square extends Shape {

    @Override
    protected void initVertexBuffer() {
        createVertexBuffer(new float[]{
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f,
        });
    }

    @Override
    protected void onDraw(GL10 gl) {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mCoords.length/3);
    }
}
