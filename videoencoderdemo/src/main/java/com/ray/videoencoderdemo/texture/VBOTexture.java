package com.ray.videoencoderdemo.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.ray.videoencoderdemo.R;
import com.ray.videoencoderdemo.opengl.RayEGLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-07 17:58
 *  description : 使用VBO 缓存顶点数据
 */
public class VBOTexture implements RayEGLSurfaceView.RayGLRenderer {

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
            0f, 0f,
            0f, 1f,
            1f, 0f,
            1f, 1f
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
    private Context mContext;
    private int mVBOId;

    public VBOTexture(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated() {
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
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fengj);
        int[] vbos = new int[1];
        glGenBuffers(1, vbos, 0);
        mVBOId = vbos[0];
        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);

        glBufferData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4 + TEXTURE_COORS.length * 4, null, GL_STATIC_DRAW);

        glBufferSubData(GL_ARRAY_BUFFER, 0, VERTEX_COORDS.length * 4, vertexBuffer);
        glBufferSubData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4, TEXTURE_COORS.length * 4, textureCoordsBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private Bitmap mBitmap;

    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            glGenTextures(1, texture, 0);
            //绑定纹理
            glBindTexture(GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            //通过以上参数生成纹理
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }

        return 0;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(mProgram);
        glUniform1i(mGlHTexture, 0);
        createTexture();
        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);
        glEnableVertexAttribArray(vPositionHandle);
        glVertexAttribPointer(vPositionHandle, 2, GL_FLOAT, false, 8, 0);
        glEnableVertexAttribArray(vCoordinateHandle);
        glVertexAttribPointer(vCoordinateHandle, 2, GL_FLOAT, false, 8, VERTEX_COORDS.length * 4);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEX_COORDS.length / 2);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
