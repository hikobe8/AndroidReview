package com.ray.opengl.camera;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ray.opengl.filter.AFilter;
import com.ray.opengl.filter.OesFilter;
import com.ray.opengl.util.MyGLUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-25 16:22
 *  description : 
 */
public class CameraDrawer implements GLSurfaceView.Renderer {

    private float[] mMatrix = new float[16];
    private SurfaceTexture mSurfaceTexture;
    private int mPicWidth, mPicHeight, mCameraWidth, mCameraHeight;
    private int mCameraId = 1;
    private AFilter mOesFilter;

    public CameraDrawer(Resources resources) {
        mOesFilter = new OesFilter(resources);
    }

    public void setDataSize(int dataWidth, int dataHeight){
        this.mPicWidth=dataWidth;
        this.mPicHeight=dataHeight;
        calculateMatrix();
    }

    private void calculateMatrix() {
        MyGLUtil.getShowMatrix(mMatrix, mPicWidth, mPicHeight, mCameraWidth, mCameraHeight);
        if (mCameraId == 1) {
            MyGLUtil.flip(mMatrix, true, false);
            MyGLUtil.rotate(mMatrix, 90f);
        } else {
            MyGLUtil.rotate(mMatrix, 270f);
        }
        mOesFilter.setMatrix(mMatrix);
    }

    public void setViewSize(int width, int height){
        this.mCameraWidth = width;
        this.mCameraHeight = height;
        calculateMatrix();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texture = createTextureID();
        mSurfaceTexture = new SurfaceTexture(texture);
        mOesFilter.create();
        mOesFilter.setTextureId(texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        setViewSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
        mOesFilter.draw();
    }

    private int createTextureID(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    public void setCameraId(int cameraId) {
        mCameraId = cameraId;
        calculateMatrix();
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }
}
