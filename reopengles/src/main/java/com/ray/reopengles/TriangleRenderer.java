package com.ray.reopengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-23 14:27
 *  description : 
 */
public class TriangleRenderer implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;\n"
                    + "void main() {\n"
                    + "  gl_Position = vPosition;\n"
                    + "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = vec4(0.5, 0, 0, 1);\n"
                    + "}";

    private float[] mCoords = {
            0.f, 0.5f, 0f,
            -0.5f, 0.f, 0f,
            0.5f, 0f, 0f
    };

    private FloatBuffer mVertexBuffer;
    private int mProgram;
    //glsl中的vPosition的程序句柄
    private int mPositionHandle;

    private int loadShader(int type, String code){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f,0f);
        //1创建顶点坐标
        mVertexBuffer = ByteBuffer
                .allocateDirect(4 * mCoords.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mCoords);
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
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 12, mVertexBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
