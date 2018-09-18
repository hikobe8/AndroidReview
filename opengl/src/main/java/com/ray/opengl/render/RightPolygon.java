package com.ray.opengl.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 正多边形
 */
public class RightPolygon extends Shape {

    private static final int COORDINATE_PER_VERTEX = 3;

    private float triangleCoordinates[];

    {
        createPolygonCoordinates();
    }

    private void createPolygonCoordinates() {
        int count = new Random().nextInt(98) + 3;
        triangleCoordinates = new float[count*(COORDINATE_PER_VERTEX + 2)];
        float degree = 360f / count;
        //设置中心点
        triangleCoordinates[0] = 0f;
        triangleCoordinates[1] = 0f;
        triangleCoordinates[2] = 0f;
        int i = 1;
        for (; i <= count ; i++) {
            triangleCoordinates[i * 3] = ((float) Math.cos((i * degree * Math.PI / 180)));
            triangleCoordinates[i * 3 + 1] = ((float) Math.sin((i * degree * Math.PI / 180)));
            triangleCoordinates[i * 3 + 2] = 0f;
        }
        triangleCoordinates[i * 3] = triangleCoordinates[3];
        triangleCoordinates[i * 3 + 1] = triangleCoordinates[4];
        triangleCoordinates[i * 3 + 2] = 0f;
    }

    protected float color[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //白色

    private int mPositionHandle;
    private int mColorHandle;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mMatrixHandler;
    private int mVertexStride = COORDINATE_PER_VERTEX * 4;

    @Override
    public float[] getCoordinatesArray() {
        return triangleCoordinates;
    }

    @Override
    public String getVertexShaderCodeString() {
        return "attribute vec4 vPosition;\n" +
                "uniform mat4 vMatrix;\n" +
                "void main() {\n" +
                "    gl_Position = vMatrix*vPosition;\n" +
                "}";
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
        //获取顶点着色器的vMatrix成员句柄
        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
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
        //启用三角形顶点的句柄
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //准备三角形的坐标数据
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDINATE_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                mVertexStride, mVertexBuffer);
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, triangleCoordinates.length/3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
