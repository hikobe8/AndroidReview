package com.ray.reopengles.simplegraph;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-23 14:27
 *  description : 
 */
public class Triangle extends Shape {

    @Override
    protected void initVertexBuffer() {
        createVertexBuffer(new float[]{
                0.f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f
        });
    }

    @Override
    protected void onDraw(GL10 gl) {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
