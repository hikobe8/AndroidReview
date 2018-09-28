package com.ray.opengl.camera.sticker;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

import com.ray.opengl.camera.TextureFilter;
import com.ray.opengl.filter.AFilter;
import com.ray.opengl.filter.NoFilter;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-28 15:51
 *  description : 
 */
public class TextureController implements GLSurfaceView.Renderer {

    private Object surface;
    private GLView mGLView;
    private Context mContext;
    private Renderer mRenderer;
    //用于渲染输出的Filter
    private AFilter mShowFilter;
    //特效处理的Filter
    private TextureFilter mEffectFilter;
    //数据的尺寸
    private Point mDataSize;
    //输出视图的大小
    private Point mWindowSize;

    public TextureController(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mGLView = new GLView(mContext);
        //避免GLView attach / detach 崩溃
        new ViewGroup(mContext) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        }.addView(mGLView);
        mShowFilter = new NoFilter(mContext.getResources());
        mGLView.attachedToWindow();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mShowFilter.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    public void setImageDirection(int cameraId) {

    }

    public void setDataSize(int width, int height) {
        mDataSize.x = width;
        mDataSize.y = height;
    }

    //当前不加任何特效
    public SurfaceTexture getTexture() {
        return mEffectFilter.getTexture();
    }

    public void requestRender() {
        mGLView.requestRender();
    }

    public void surfaceCreated(Object nativeWindow) {
        this.surface = nativeWindow;
        mGLView.surfaceCreated(null);
    }

    public void setRenderer(Renderer renderer) {
        mRenderer = renderer;
    }

    public void onResume() {
        mGLView.onResume();
    }

    public void onPause() {
        mGLView.onPause();
    }

    public void onDestroy() {
        if (mRenderer != null) {
            mRenderer.onDestroy();
        }
        mGLView.surfaceDestroyed(null);
        mGLView.detachedFromWindow();
        mGLView.clear();
    }

    public void surfaceChanged(int width, int height) {
        this.mWindowSize.x = width;
        this.mWindowSize.y = height;
        mGLView.surfaceChanged(null, 0, width, height);
    }

    public void surfaceDestroyed() {
        mGLView.surfaceDestroyed(null);
    }

    private class GLView extends GLSurfaceView {

        public GLView(Context context) {
            super(context);
            init();
        }

        private void init() {
            getHolder().addCallback(null);
            setEGLWindowSurfaceFactory(new GLSurfaceView.EGLWindowSurfaceFactory(){

                @Override
                public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object nativeWindow) {
                    return egl.eglCreateWindowSurface(display, config, surface, null);
                }

                @Override
                public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
                    egl.eglDestroySurface(display, surface);
                }
            });
            setEGLContextClientVersion(2);
            setRenderer(TextureController.this);
            setRenderMode(RENDERMODE_WHEN_DIRTY);
            setPreserveEGLContextOnPause(true);
        }

        public void attachedToWindow() {
            super.onAttachedToWindow();
        }

        public void detachedFromWindow(){
            super.onDetachedFromWindow();
        }

        public void clear() {

        }
    }

}
