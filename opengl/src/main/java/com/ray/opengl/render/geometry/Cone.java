package com.ray.opengl.render.geometry;

import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 圆锥形
 */
public class Cone extends Shape {

    private static final int COORDINATE_PER_VERTEX = 3;

    private float triangleCoordinates[];
    private Oval mOval;

    {
        createPolygonCoordinates();
        mOval = new Oval();
    }

    private void createPolygonCoordinates() {
        int count = 360;
        triangleCoordinates = new float[count*(COORDINATE_PER_VERTEX + 2)];
        float degree = 360f / count;
        //设置中心点
        triangleCoordinates[0] = 0f;
        triangleCoordinates[1] = 0f;
        triangleCoordinates[2] = 2f;
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

    private int mPositionHandle;
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
        return "uniform mat4 vMatrix;\n" +
                "varying vec4 vColor;\n" +
                "attribute vec4 vPosition;\n" +
                "\n" +
                "void main(){\n" +
                "    gl_Position=vMatrix*vPosition;\n" +
                "    if(vPosition.z!=0.0){\n" +
                "        vColor=vec4(0.0,0.0,0.0,1.0);\n" +
                "    }else{\n" +
                "        vColor=vec4(0.9,0.9,0.9,1.0);\n" +
                "    }\n" +
                "}";
    }

    @Override
    public String getFragmentShaderCodeString() {
        return "precision mediump float;\n" +
                "varying vec4 vColor;\n" +
                "void main(){\n" +
                "    gl_FragColor=vColor;\n" +
                "}";
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //获取顶点着色器的vMatrix成员句柄
        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mOval.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        //计算宽高比
        float ratio = width*1f/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, triangleCoordinates.length/3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        mOval.setMVPMatrix(mMVPMatrix);
        mOval.drawFrame(gl);
    }
}
