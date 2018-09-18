package com.ray.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 等腰直角三角形
 */
public class ColorfulTriangle extends Shape {

    private static final int COORDINATE_PER_VERTEX = 2;

    private float triangleCoordinates[] = {
            1f,  1f, 0.0f, // top
            -1f, -1f, 0.0f, // bottom left
            1f, -1f, 0.0f  // bottom right
    };

    //设置颜色
    private float color[] = {
            0.0f, 1.0f, 0.0f, 1.0f ,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    private int mPositionHandle;
    private int mColorHandle;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mMatrixHandler;
    private FloatBuffer mColorBuffer;
    private int mVertexStride = 12;

    @Override
    public float[] getCoordinatesArray() {
        return triangleCoordinates;
    }

    @Override
    public String getVertexShaderCodeString() {
        return  "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;"+
                "varying  vec4 vColor;"+
                "attribute vec4 aColor;"+
                "void main() {" +
                "  gl_Position = vMatrix*vPosition;" +
                "  vColor=aColor;"+
                "}";
    }

    @Override
    public String getFragmentShaderCodeString() {
        return  "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                color.length * 4);
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mColorBuffer = bb.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);
        super.onSurfaceCreated(gl, config);
        //获取顶点着色器的vMatrix成员句柄
        mMatrixHandler= glGetUniformLocation(mProgram,"vMatrix");
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = glGetAttribLocation(mProgram, "vPosition");
        //获取片元着色器的vColor成员的句柄
        mColorHandle = glGetAttribLocation(mProgram, "aColor");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        //计算宽高比
        float ratio = width*1f/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3 , 7);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7f, 0f, 0f, 0f,0f, 1.0f, 0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        //指定vMatrix的值
        glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //启用三角形顶点的句柄
        glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        glVertexAttribPointer(mPositionHandle, COORDINATE_PER_VERTEX,
                GL_FLOAT, false,
                mVertexStride, mVertexBuffer);
        //设置绘制三角形的颜色
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle,4,
                GLES20.GL_FLOAT,false,
                0,mColorBuffer);
        //绘制三角形
        glDrawArrays(GL_TRIANGLES, 0, 3);
        //禁止顶点数组的句柄
        glDisableVertexAttribArray(mPositionHandle);
    }
}
