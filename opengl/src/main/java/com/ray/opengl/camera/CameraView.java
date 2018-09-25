package com.ray.opengl.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-25 17:32
 *  description : 
 */
public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private CameraDrawer mCameraDrawer;
    private KitkatCamera mKitkatCamera;
    private int mCameraId = 1;
    private Runnable mRunnable;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mCameraDrawer = new CameraDrawer(getResources());
        mKitkatCamera = new KitkatCamera();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mCameraDrawer.onSurfaceCreated(gl, config);
        if (mRunnable != null) {
            mRunnable.run();
            mRunnable = null;
        }
        mKitkatCamera.open(mCameraId);
        mCameraDrawer.setCameraId(mCameraId);
        Point size = mKitkatCamera.getPreviewSize();
        mCameraDrawer.setDataSize(size.x, size.y);
        mKitkatCamera.setPreviewTexture(mCameraDrawer.getSurfaceTexture());
        mCameraDrawer.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mKitkatCamera.preview();
    }

    public void switchCamera(){
        mRunnable=new Runnable() {
            @Override
            public void run() {
                mKitkatCamera.close();
                mCameraId=mCameraId==1?0:1;
            }
        };
        onPause();
        onResume();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraDrawer.setViewSize(width, height);
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mCameraDrawer.onDrawFrame(gl);
    }

    @Override
    public void onPause() {
        super.onPause();
        mKitkatCamera.close();
    }
}
