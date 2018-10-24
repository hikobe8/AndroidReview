package com.ray.reopengles.simplegraph;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-24 18:44
 *  description : 简单图形
 */
public abstract class Shape implements GLSurfaceView.Renderer{

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;\n"
                    + "uniform mat4 uMVPMatrix;\n"
                    + "void main() {\n"
                    + "  gl_Position = uMVPMatrix * vPosition;\n"
                    + "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = vec4(0.5, 0, 0, 1);\n"
                    + "}";

    protected float[] mCoords;

    private float[] mMvpMatrix = new float[16];

    private FloatBuffer mVertexBuffer;
    private int mProgram;
    //glsl中的vPosition的程序句柄
    private int mPositionHandle;
    //glsl中的uMVPMatrix的程序句柄
    private int mMatrixHandle;

    private int loadShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //1创建顶点坐标
        initVertexBuffer();
        mVertexBuffer.position(0);
        //2创建OpenGLES program
        mProgram = GLES20.glCreateProgram();
        //3加载编译vertex, fragment shader
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        //4添加到program
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        //5链接程序
        GLES20.glLinkProgram(mProgram);
        //6使用程序
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);
    }

    protected abstract void initVertexBuffer();

    protected void createVertexBuffer(float[] coords) {
        mCoords = coords;
        mVertexBuffer = ByteBuffer
                .allocateDirect(4 * coords.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(coords);
        mVertexBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = width*1f/height;
        if (width > height) {
            Matrix.orthoM(mMvpMatrix, 0, -ratio, ratio, -1, 1, 3f, 7f);
        } else {
            Matrix.orthoM(mMvpMatrix, 0, -1, 1, -1/ratio, 1/ratio, 3f, 7f);
        }
        Matrix.translateM(mMvpMatrix, 0, 0f, 0f, -3.5f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMvpMatrix, 0);
        onDraw(gl);
    }

    protected abstract void onDraw(GL10 gl);
}
