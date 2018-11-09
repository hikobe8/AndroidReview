package com.ray.videoencoderdemo.texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-08 14:03
 *  description : 
 */
public class FBORenderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;\n"
                    + "attribute vec2 vCoordinate;\n"
                    + "varying vec2 aCoordinate;\n"
                    + "void main() {\n"
                    + "  gl_Position = vPosition;\n"
                    + "  aCoordinate = vCoordinate;\n"
                    + "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;\n"
                    + "uniform sampler2D vTexture;\n"
                    + "varying vec2 aCoordinate;\n"
                    + "void main() {\n"
                    + "   gl_FragColor=texture2D(vTexture,aCoordinate);\n"
                    + "}";

    private static final float[] VERTEX_COORDS = {
            -1f, 1f,
            -1f, -1f,
            1f, 1f,
            1f, -1f
    };

    private static final float[] TEXTURE_COORS = {
            0f, 1f,
            0f, 0f,
            1f, 1f,
            1f, 0f
    };

    private int loadShader(int type, String code) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);
        return shader;
    }

    private int mProgram;
    private int vPositionHandle;
    private int vCoordinateHandle;
    private int mGlHTexture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordsBuffer;
    private int mVBOId;

    public void onCreate(){
        vertexBuffer = ByteBuffer.allocateDirect(VERTEX_COORDS.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX_COORDS);
        vertexBuffer.position(0);
        textureCoordsBuffer = ByteBuffer.allocateDirect(TEXTURE_COORS.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEXTURE_COORS);
        textureCoordsBuffer.position(0);
        int vertex = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragment = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        mProgram = glCreateProgram();
        glAttachShader(mProgram, vertex);
        glAttachShader(mProgram, fragment);
        glLinkProgram(mProgram);
        vPositionHandle = glGetAttribLocation(mProgram, "vPosition");
        vCoordinateHandle = glGetAttribLocation(mProgram, "vCoordinate");
        mGlHTexture = glGetUniformLocation(mProgram, "vTexture");
        //生成VBO
        int[] vbos = new int[1];
        glGenBuffers(1, vbos, 0);
        mVBOId = vbos[0];

        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);

        glBufferData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4 + TEXTURE_COORS.length * 4, null, GL_STATIC_DRAW);

        glBufferSubData(GL_ARRAY_BUFFER, 0, VERTEX_COORDS.length * 4, vertexBuffer);
        glBufferSubData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4, TEXTURE_COORS.length * 4, textureCoordsBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void onSurfaceChanged(int width, int height) {
        glViewport(0, 0, width, height);
    }

    public void onDraw(int textureId){
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(1f, 0f, 0f, 1f);
        glUseProgram(mProgram);
        glUniform1i(mGlHTexture, 0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);
        glEnableVertexAttribArray(vPositionHandle);
        glVertexAttribPointer(vPositionHandle, 2, GL_FLOAT, false, 8, 0);
        glEnableVertexAttribArray(vCoordinateHandle);
        glVertexAttribPointer(vCoordinateHandle, 2, GL_FLOAT, false, 8, VERTEX_COORDS.length * 4);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEX_COORDS.length / 2);
        //解绑VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        //解绑纹理
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisableVertexAttribArray(vPositionHandle);
        glDisableVertexAttribArray(vCoordinateHandle);
    }

}
