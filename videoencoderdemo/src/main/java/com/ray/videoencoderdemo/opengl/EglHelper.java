package com.ray.videoencoderdemo.opengl;

import android.util.Log;
import android.view.Surface;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-11-06 17:07
 *  description : Egl环境工具类
 */
public class EglHelper {

    private final static boolean LOG_EGL = true;
    private EGL10 mEgl;
    private EGLDisplay mEglDisplay;
    private EGLContext mEglContext;
    private EGLSurface mEglSurface;

    public void initEgl(Surface surface, EGLContext eglContext) {

        if (LOG_EGL) {
            Log.w("EglHelper", "start() tid=" + Thread.currentThread().getId());
        }
        /*
         * Get an EGL instance
         */
        mEgl = (EGL10) EGLContext.getEGL();

        /*
         * Get to the default display.
         */
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }

        /*
         * We can now initialize EGL for that display
         */
        int[] version = new int[2];
        if(!mEgl.eglInitialize(mEglDisplay, version)) {
            throw new RuntimeException("eglInitialize failed");
        }

        /*
         * config attributes
         */
        int [] attrbutes = new int[] {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 8,
                EGL10.EGL_STENCIL_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_NONE
        };

        int[] num_config = new int[1];
        if (!mEgl.eglChooseConfig(mEglDisplay, attrbutes, null, 0,
                num_config)) {
            throw new IllegalArgumentException("eglChooseConfig failed");
        }
        int numConfigs = num_config[0];

        if (numConfigs <= 0) {
            throw new IllegalArgumentException(
                    "No configs match configSpec");
        }

        EGLConfig[] configs = new EGLConfig[numConfigs];
        if (!mEgl.eglChooseConfig(mEglDisplay, attrbutes, configs, numConfigs,
                num_config)) {
            throw new IllegalArgumentException("eglChooseConfig#2 failed");
        }

        if (eglContext != null) {
            mEglContext = mEgl.eglCreateContext(mEglDisplay, configs[0], eglContext, null);
        } else {
            mEglContext = mEgl.eglCreateContext(mEglDisplay, configs[0], EGL10.EGL_NO_CONTEXT, null);
        }

        mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, configs[0], surface, null);

        if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            throw new RuntimeException("eglMakeCurrent fail");
        }

    }

    public boolean swapBuffers() {
        if (mEgl != null) {
            return mEgl.eglSwapBuffers(mEglDisplay, mEglSurface);
        } else {
            throw new RuntimeException("egl is null");
        }
    }

    public EGLContext getEglContext() {
        return mEglContext;
    }

    public void destroyEgl() {
        if (mEglSurface != null && mEglSurface != EGL10.EGL_NO_SURFACE) {
            mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_CONTEXT);

            mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
            mEglSurface = null;

            mEgl.eglDestroyContext(mEglDisplay, mEglContext);
            mEglContext = null;

            mEgl.eglTerminate(mEglDisplay);
            mEglDisplay = null;
            mEgl = null;

        }
    }

}
