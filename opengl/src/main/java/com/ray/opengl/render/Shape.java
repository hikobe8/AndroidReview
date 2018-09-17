package com.ray.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 17:01
 *  description : 
 */
public abstract class Shape implements GLSurfaceView.Renderer {

    private static final int COORDS_PER_VERTEX = 2;
    protected static String sVertexString = "attribute vec4 vPosition;\n" +
            " void main() {\n" +
            "     gl_Position = vPosition;\n" +
            " }";

    protected static String sFragmentString = "precision mediump float;\n" +
            " uniform vec4 vColor;\n" +
            " void main() {\n" +
            "     gl_FragColor = vColor;\n" +
            " }";

    protected float triangleCoords[] = {
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    protected float color[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //白色
    
    protected FloatBuffer mVertexBuffer;
    protected int mProgram;
    private int mPositionHandle;
    private int mColorHandle;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //将背景设置为灰色
        glClearColor(0.5f,0.5f,0.5f,1.0f);
        //申请底层空间
        float[] coordinates = getCoordinatesArray();
        ByteBuffer bb = ByteBuffer.allocateDirect(
                coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(coordinates);
        mVertexBuffer.position(0);
        int vertexShader = loadShader(GL_VERTEX_SHADER,
                getVertexShaderCodeString());
        int fragmentShader = loadShader(GL_FRAGMENT_SHADER,
                getFragmentShaderCodeString());
        //创建一个空的gles 程序
        mProgram = glCreateProgram();
        //把定点着色器加到程序中
        glAttachShader(mProgram, vertexShader);
        //把片元着色器加到程序中
        glAttachShader(mProgram, fragmentShader);
        //链接到着色器程序
        glLinkProgram(mProgram);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    private int loadShader(int type, String shaderCodeString) {
        //根据type创建顶点着色器或者片元着色器
        int shader = glCreateShader(type);
        //将资源加入到着色器中，并编译
        glShaderSource(shader, shaderCodeString);
        glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                2, mVertexBuffer);
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public abstract float[] getCoordinatesArray();

    public abstract String getVertexShaderCodeString();

    public abstract String getFragmentShaderCodeString();

}
