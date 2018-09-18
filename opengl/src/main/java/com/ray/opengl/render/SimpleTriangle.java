package com.ray.opengl.render;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 
 */
public class SimpleTriangle extends Shape {

    private static final int COORDINATE_PER_VERTEX = 2;

    private float triangleCoordinates[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    protected float color[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //白色

    private int mPositionHandle;
    private int mColorHandle;
    private int mVertexStride = 12;

    @Override
    public float[] getCoordinatesArray() {
        return triangleCoordinates;
    }

    @Override
    public String getVertexShaderCodeString() {
        return "attribute vec4 vPosition;\n" +
                    " void main() {\n" +
                    "     gl_Position = vPosition;\n" +
                    " }";
    }

    @Override
    public String getFragmentShaderCodeString() {
        return "precision mediump float;\n" +
                    " uniform vec4 vColor;\n" +
                    " void main() {\n" +
                    "     gl_FragColor = vColor;\n" +
                    " }";
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDINATE_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mVertexStride, mVertexBuffer);
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
