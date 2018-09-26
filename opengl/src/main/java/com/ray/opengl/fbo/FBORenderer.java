package com.ray.opengl.fbo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.ray.opengl.filter.AFilter;
import com.ray.opengl.filter.GrayFilter;
import com.ray.opengl.util.MyGLUtil;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class FBORenderer implements GLSurfaceView.Renderer {

    private AFilter mFilter;
    private Bitmap mBitmap;
    private ByteBuffer mBuffer;
    private int[] fFrame = new int[1];
    private int[] fRender = new int[1];
    private int[] fTexture = new int[2];
    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    interface Callback{
        void onCall(ByteBuffer data);
    }

    public FBORenderer(Resources resources) {
        mFilter = new GrayFilter(resources);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mFilter.create();
        mFilter.setMatrix(MyGLUtil.flip(MyGLUtil.getOriginalMatrix(), false, true));
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            createFBOEnv();
            glBindFramebuffer(GL_FRAMEBUFFER, fFrame[0]);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                    GL_TEXTURE_2D, fTexture[1], 0);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, fRender[0]);
            glViewport(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            mFilter.setTextureId(fTexture[0]);
            mFilter.draw();
            glReadPixels(0, 0, mBitmap.getWidth(), mBitmap.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, mBuffer);
            if (mCallback != null) {
                mCallback.onCall(mBuffer);
            }
            deleteFBOEnv();
            mBitmap.recycle();
        }
    }

    private void createFBOEnv() {
        //生成frame buffer
        glGenFramebuffers(1, fFrame, 0);
        //生成render buffer
        glGenRenderbuffers(1, fRender, 0);
        //绑定 render buffer
        glBindRenderbuffer(GL_RENDERBUFFER, fRender[0]);
        glRenderbufferStorage(GL_RENDERBUFFER_DEPTH_SIZE, GL_DEPTH_COMPONENT16, mBitmap.getWidth(), mBitmap.getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, fRender[0]);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        glGenTextures(2, fTexture, 0);
        for (int i = 0; i < 2; i++) {
            glBindTexture(GL_TEXTURE_2D, fTexture[i]);
            if (i ==0) {
                //输入 纹理
                GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, mBitmap, 0);
            } else {
                //输出 纹理
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, mBitmap.getWidth(), mBitmap.getHeight(),
                        0, GL_RGBA, GL_UNSIGNED_BYTE,null);
            }
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        }
        mBuffer = ByteBuffer.allocate(mBitmap.getWidth()*mBitmap.getHeight()*4);
    }

    private void deleteFBOEnv() {
        glDeleteTextures(2, fTexture, 0);
        glDeleteFramebuffers(1, fFrame, 0);
        glDeleteRenderbuffers(1, fRender, 0);
    }

    public void setBitmap(Bitmap bmp) {
        mBitmap  = bmp;
    }
}
