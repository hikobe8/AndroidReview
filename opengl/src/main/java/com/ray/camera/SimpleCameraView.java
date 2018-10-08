package com.ray.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-08 14:20
 *  description : 
 */
public class SimpleCameraView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;

    public SimpleCameraView(Context context) {
        this(context, null, 0);
    }

    public SimpleCameraView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SimpleCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open(0);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void startPreview(){
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    public void releasePreview(){
        if (mCamera != null) {
            mCamera.release();
        }
    }

}
