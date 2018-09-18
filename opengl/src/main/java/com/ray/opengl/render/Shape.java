package com.ray.opengl.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glViewport;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 17:01
 *  description : 
 */
public abstract class Shape implements GLSurfaceView.Renderer {

    protected FloatBuffer mVertexBuffer;

    protected int mProgram;
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
    }

    protected int loadShader(int type, String shaderCodeString) {
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
    }

    public abstract float[] getCoordinatesArray();

    public abstract String getVertexShaderCodeString();

    public abstract String getFragmentShaderCodeString();

}
