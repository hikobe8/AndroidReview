package com.ray.opengl.render.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.ray.opengl.util.ShaderHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-20 15:34
 *  description : 
 */
public class SimpleTextureShader implements GLSurfaceView.Renderer {

    private Context mContext;
    private int mProgram;
    private Bitmap mBitmap;
    private final float[] mProjectMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private FloatBuffer mPosBuffer;
    private FloatBuffer mCoordBuffer;
    private int mGlHPosition;
    private int mGlHTexture;
    private int mGlHCoordinate;
    private int mGlHMatrix;
    private int mGLVChangeType;
    private int mGLVChangeColor;
    private int mGLUxy;

    private Filter mFilter = new Filter(0, new float[]{0f, 0f, 0f});

    private final float[] sPos = {
            -1.0f, 1.0f,    //左上角
            -1.0f, -1.0f,   //左下角
            1.0f, 1.0f,     //右上角
            1.0f, -1.0f     //右下角
    };

    //纹理坐标
    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    private boolean mRefresh;
    private GL10 mGl;
    private EGLConfig mConfig;
    private int mWidth;
    private int mHeight;
    private float mViewRatio;

    public void setFilter(Filter filter) {
       mFilter = filter;
       if (filter.mType > 2) {
           mRefresh = true;
       }
    }

    public SimpleTextureShader(Context context) {
        mContext = context;
        mPosBuffer = createFloatBuffer(sPos);
        mCoordBuffer = createFloatBuffer(sCoord);
        try {
            InputStream inputStream = context.getAssets().open("texture/fengj.png");
            mBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mGl = gl;
        mConfig = config;
        //将背景设置为灰色
        glClearColor(0.5f,0.5f,0.5f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram = ShaderHelper.getLinkedOpenGLESProgram(
                ShaderHelper.loadShaderCode(mContext, "vShader/texture_vertex_shader.glsl"),
                ShaderHelper.loadShaderCode(mContext, "fShader/texture_fragment_shader.glsl")
        );
        mGlHPosition= glGetAttribLocation(mProgram,"vPosition");
        mGlHCoordinate= glGetAttribLocation(mProgram,"vCoordinate");
        mGlHTexture= glGetUniformLocation(mProgram,"vTexture");
        mGlHMatrix= glGetUniformLocation(mProgram,"vMatrix");
        mGLVChangeType = glGetUniformLocation(mProgram, "vChangeType");
        mGLVChangeColor = glGetUniformLocation(mProgram, "vChangeColor");
        mGLUxy = glGetUniformLocation(mProgram, "uXY");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        glViewport(0, 0, width, height);
        //设置透视矩阵
        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();
        float imageRatio = bitmapWidth * 1f / bitmapHeight;
        mViewRatio = width * 1f / height;
        if (width > height) {
            //横屏模式
            if (imageRatio > mViewRatio) {
                Matrix.orthoM(mProjectMatrix, 0, -mViewRatio * imageRatio, mViewRatio * imageRatio, -1, 1, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -mViewRatio / imageRatio, mViewRatio / imageRatio, -1, 1, 3, 7);
            }
        } else {
            if (imageRatio > mViewRatio) {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1 / mViewRatio * imageRatio, 1 / mViewRatio * imageRatio, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -mViewRatio / imageRatio, mViewRatio / imageRatio, 3, 7);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mRefresh && mWidth != 0 && mHeight != 0) {
            onSurfaceCreated(mGl, mConfig);
            onSurfaceChanged(mGl, mWidth, mHeight);
            mRefresh = false;
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(mProgram);
        glUniform1i(mGLVChangeType, mFilter.mType);
        glUniform3fv(mGLVChangeColor, 1, mFilter.changeColor, 0);
        glUniform1f(mGLUxy, mViewRatio);
        glUniformMatrix4fv(mGlHMatrix,1,false,mMVPMatrix,0);
        glEnableVertexAttribArray(mGlHPosition);
        glEnableVertexAttribArray(mGlHCoordinate);
        glUniform1i(mGlHTexture, 0);
        int textureId = createTexture();
        //传入顶点坐标
        glVertexAttribPointer(mGlHPosition,2, GL_FLOAT,false,0,mPosBuffer);
        //传入纹理坐标
        glVertexAttribPointer(mGlHCoordinate,2, GL_FLOAT,false,0,mCoordBuffer);
        glDrawArrays(GL_TRIANGLE_STRIP,0,4);
        glDisableVertexAttribArray(mGlHPosition);
        glDisableVertexAttribArray(mGlHCoordinate);
    }

    private FloatBuffer createFloatBuffer(float[] arr) {
        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(arr.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(arr);
        floatBuffer.position(0);
        return floatBuffer;
    }

    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            glGenTextures(1, texture, 0);
            //生成纹理
            glBindTexture(GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }

}
