package com.ray.videoencoderdemo.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.ray.videoencoderdemo.R;
import com.ray.videoencoderdemo.opengl.RayEGLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_FRAMEBUFFER_COMPLETE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glCheckFramebufferStatus;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

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
                    + "uniform mat4 uMVPMatrix;\n"
                    + "void main() {\n"
                    + "  gl_Position = uMVPMatrix * vPosition;\n"
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
    private int mMatrixHandle;

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
    private int mFBOId;
    private int mImgTextureId;
    private int mTextureId;
    private FBORenderer mFBORenderer;
    private float[] mMatrix = new float[16];

    public VBOTexture(Context context) {
        mContext = context;
        mFBORenderer = new FBORenderer();
    }

    public void onSurfaceCreated() {
        mFBORenderer.onCreate();
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
        mMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.lake);

        //生成VBO
        int[] vbos = new int[1];
        glGenBuffers(1, vbos, 0);
        mVBOId = vbos[0];

        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);

        glBufferData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4 + TEXTURE_COORS.length * 4, null, GL_STATIC_DRAW);

        glBufferSubData(GL_ARRAY_BUFFER, 0, VERTEX_COORDS.length * 4, vertexBuffer);
        glBufferSubData(GL_ARRAY_BUFFER, VERTEX_COORDS.length * 4, TEXTURE_COORS.length * 4, textureCoordsBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //创建FBO
        int[] fbos = new int[1];
        glGenBuffers(1, fbos, 0);
        mFBOId = fbos[0];
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOId);

        int[] texture = new int[1];
        glGenTextures(1, texture, 0);
        mTextureId = texture[0];
        //绑定纹理
        glBindTexture(GL_TEXTURE_2D, texture[0]);
        glActiveTexture(GL_TEXTURE0);
        glUniform1i(mGlHTexture, 0);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        //通过以上参数生成纹理
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, mBitmap.getWidth(), mBitmap.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture[0], 0);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            Log.e("FBORenderer", "fbo error");
        } else {
            Log.e("FBORenderer", "fbo success");
        }

        //解绑纹理
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        mImgTextureId = createTexture();
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
            //解绑纹理
            glBindTexture(GL_TEXTURE_2D, 0);
            return texture[0];
        }

        return 0;
    }

    private int mWidth, mHeight;

    public void onSurfaceChanged(int width, int height) {
//        glViewport(0, 0, width, height);
//        mFBORenderer.onSurfaceChanged(width, height);
        mWidth = width;
        mHeight = height;
        float ratio = width * 1f / height;
        if (width < height) {
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -1 / ratio * mBitmap.getWidth() * 1f / mBitmap.getHeight(), 1 / ratio * mBitmap.getWidth() * 1f / mBitmap.getHeight(), -1f, 1f);
        } else {
            Matrix.orthoM(mMatrix, 0, -mBitmap.getHeight() * 1f / mBitmap.getWidth() * ratio, mBitmap.getHeight() * 1f / mBitmap.getWidth() * ratio, -1f, 1f, -1f, 1f);
        }
        Matrix.rotateM(mMatrix, 0, 180f, 1f, 0f, 0f);
    }

    public void onDrawFrame() {
        glViewport(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(1f, 0f, 0f, 1f);
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOId);
        glUseProgram(mProgram);
        glUniform1i(mGlHTexture, 0);
        glUniformMatrix4fv(mMatrixHandle, 1, false, mMatrix, 0);
        glBindTexture(GL_TEXTURE_2D, mImgTextureId);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOId);
        glEnableVertexAttribArray(vPositionHandle);
        glVertexAttribPointer(vPositionHandle, 2, GL_FLOAT, false, 8, 0);
        glEnableVertexAttribArray(vCoordinateHandle);
        glVertexAttribPointer(vCoordinateHandle, 2, GL_FLOAT, false, 8, VERTEX_COORDS.length * 4);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEX_COORDS.length / 2);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0,mWidth, mHeight);
        mFBORenderer.onDraw(mTextureId);
    }
}
