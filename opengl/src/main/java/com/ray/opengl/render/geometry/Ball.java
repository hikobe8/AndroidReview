package com.ray.opengl.render.geometry;

import android.opengl.Matrix;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 圆柱体
 */
public class Ball extends Shape {

    private static final int COORDINATE_PER_VERTEX = 3;

    private float coordinates[];

    {
        createPolygonCoordinates();
    }

    private void createPolygonCoordinates() {
        int step = 1;
        ArrayList<Float> data=new ArrayList<>();
        float r1,r2;
        float h1,h2;
        float sin,cos;
        for(float i=-90;i<90+step;i+=step){
            r1 = (float)Math.cos(i * Math.PI / 180.0);
            r2 = (float)Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(i * Math.PI / 180.0);
            h2 = (float)Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2=step*2;
            for (float j = 0.0f; j <360.0f+step; j +=step2 ) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        coordinates=new float[data.size()];
        for(int i=0;i<coordinates.length;i++){
            coordinates[i]=data.get(i);
        }
    }

    private int mPositionHandle;
    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private int mMatrixHandler;
    private int mVertexStride = COORDINATE_PER_VERTEX * 4;

    @Override
    public float[] getCoordinatesArray() {
        return coordinates;
    }

    @Override
    public String getVertexShaderCodeString() {
        return "uniform mat4 vMatrix;\n" +
                "varying vec4 vColor;\n" +
                "attribute vec4 vPosition;\n" +
                "\n" +
                "void main(){\n" +
                "    gl_Position=vMatrix*vPosition;\n" +
                "    float color;\n" +
                "    if(vPosition.z>0.0){\n" +
                "        color=vPosition.z;\n" +
                "    }else{\n" +
                "        color=-vPosition.z;\n" +
                "    }\n" +
                "    vColor=vec4(color,color,color,1.0);\n" +
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
        glEnable(GL_DEPTH_TEST);
        //获取顶点着色器的vMatrix成员句柄
        mMatrixHandler= glGetUniformLocation(mProgram,"vMatrix");
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = glGetAttribLocation(mProgram, "vPosition");
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
        glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //准备三角形的坐标数据
        glEnableVertexAttribArray(mPositionHandle);
        glVertexAttribPointer(mPositionHandle, COORDINATE_PER_VERTEX,
                GL_FLOAT, false,
                mVertexStride, mVertexBuffer);
        //绘制三角形
        glDrawArrays(GL_TRIANGLE_FAN, 0, coordinates.length/3);
        //禁止顶点数组的句柄
        glDisableVertexAttribArray(mPositionHandle);
    }
}
