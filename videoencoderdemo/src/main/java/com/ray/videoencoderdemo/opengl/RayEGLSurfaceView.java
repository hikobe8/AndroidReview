package com.ray.videoencoderdemo.opengl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLContext;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-07 9:46
 *  description : 
 */
public class RayEGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    private int mRenderMode = RENDERMODE_CONTINUOUSLY;
    private EGLThread mEGLThread;
    private Surface mSurface;
    private EGLContext mEGLContext;
    private RayGLRenderer mGLRenderer;

    public RayEGLSurfaceView(Context context) {
        this(context, null);
    }

    public RayEGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RayEGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public void setRenderer(RayGLRenderer renderer) {
        mGLRenderer = renderer;
    }

    public void setRenderMode(int renderMode) {
        if (mGLRenderer == null) {
            throw new RuntimeException("renderer is null!");
        }
        mRenderMode = renderMode;
    }

    public void setSurfaceAndEglContext(Surface surface, EGLContext eglContext) {
        this.mSurface = surface;
        this.mEGLContext = eglContext;
    }

    public EGLContext getEglContext() {
        if (mEGLThread != null) {
            return mEGLThread.getEglContext();
        }
        return null;
    }

    public void requestRender() {
        if (mEGLThread != null) {
            mEGLThread.requestRender();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mSurface == null) {
            mSurface = holder.getSurface();
        }
        mEGLThread = new EGLThread(new WeakReference<>(this));
        mEGLThread.mIsCreate = true;
        mEGLThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mEGLThread.width = width;
        mEGLThread.height = height;
        mEGLThread.mIsChange = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mEGLThread.onDestroy();
        mEGLThread = null;
        mSurface = null;
        mEGLContext = null;
    }

    static class EGLThread extends Thread {

        private WeakReference<RayEGLSurfaceView> mRayEGLSurfaceViewWeakReference;
        private EglHelper mEglHelper;
        private boolean mIsExit = false;
        private boolean mIsCreate = false;
        private boolean mIsChange = false;
        private boolean mIsStart = false;

        private int width;
        private int height;
        private final Object lock = new Object();

        public EGLThread(WeakReference<RayEGLSurfaceView> rayEGLSurfaceViewWeakReference) {
            mRayEGLSurfaceViewWeakReference = rayEGLSurfaceViewWeakReference;
        }

        @Override
        public void run() {
            super.run();
            mIsExit = false;
            mEglHelper = new EglHelper();
            mEglHelper.initEgl(mRayEGLSurfaceViewWeakReference.get().mSurface, mRayEGLSurfaceViewWeakReference.get().mEGLContext);
            while (true) {

                if (mIsExit) {
                    release();
                    break;
                }

                if (mIsStart) {
                    if (mRayEGLSurfaceViewWeakReference.get().mRenderMode == RENDERMODE_WHEN_DIRTY) {
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (mRayEGLSurfaceViewWeakReference.get().mRenderMode == RENDERMODE_CONTINUOUSLY) {
                        try {
                            Thread.sleep(1000/60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new RuntimeException("wrong render mode!");
                    }
                }

                onCreate();
                onChange(width, height);
                onDraw();

                mIsStart = true;
            }
        }

        private void requestRender(){
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        public void onDestroy() {
            mIsExit = true;
            requestRender();
        }

        public EGLContext getEglContext() {
            if (mEglHelper != null) {
                return mEglHelper.getEglContext();
            }
            return null;
        }

        private void onDraw() {
            if (mRayEGLSurfaceViewWeakReference.get().mGLRenderer != null && mEglHelper != null) {
                mRayEGLSurfaceViewWeakReference.get().mGLRenderer.onDrawFrame();
                if (!mIsStart) {
                    mRayEGLSurfaceViewWeakReference.get().mGLRenderer.onDrawFrame();
                }
                mEglHelper.swapBuffers();
            }
        }

        private void onChange(int width, int height) {
            if (mIsChange && mRayEGLSurfaceViewWeakReference.get().mGLRenderer != null) {
                mIsChange = false;
                mRayEGLSurfaceViewWeakReference.get().mGLRenderer.onSurfaceChanged(width, height);
            }
        }

        private void onCreate() {
            if (mIsCreate && mRayEGLSurfaceViewWeakReference.get().mGLRenderer != null) {
                mIsCreate = false;
                mRayEGLSurfaceViewWeakReference.get().mGLRenderer.onSurfaceCreated();
            }
        }

        private void release() {
            if (mEglHelper != null) {
                mEglHelper.destroyEgl();
                mEglHelper = null;
                mRayEGLSurfaceViewWeakReference.clear();
                mRayEGLSurfaceViewWeakReference = null;
            }
        }

    }

    public interface RayGLRenderer{
        void onSurfaceCreated();
        void onSurfaceChanged(int width, int height);
        void onDrawFrame();
    }

}
