package com.ray.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-27 11:04
 *  description : 
 */
public class EGLHelper {

    public EGL10 mEGL10;
    public EGLDisplay mEGLDisplay;
    public EGLConfig mEGLConfig;
    public EGLSurface mEGLSurface;
    public EGLContext mEGLContext;
    public GL10 mGL10;

    private static final int EGL_CONTEXT_CLIENT_VERSION=0X3098;

    private static final int SURFACE_PBUFFER = 1;
    private static final int SURFACE_PIM = 2;
    private static final int SURFACE_WINDOW = 3;

    private int mSurfaceType = SURFACE_PBUFFER;
    private Object mSurfaceNativeObj;

    private int red = 8;
    private int green = 8;
    private int blue = 8;
    private int alpha = 8;
    private int depth = 16;
    private int renderType = 4;
    private int bufferType = EGL10.EGL_SINGLE_BUFFER;
    private EGLContext shareContext = EGL10.EGL_NO_CONTEXT;

    public void config(int red, int green, int blue, int alpha,
                       int depth, int renderType){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.depth = depth;
        this.renderType = renderType;
    }

    public void setSurfaceType(int type, Object ... obj) {
        this.mSurfaceType = type;
        if (obj != null) {
            this.mSurfaceNativeObj = obj[0];
        }
    }

    public GLError eglInit(int width, int height) {
        int[] attributes = new int[] {
            EGL10.EGL_RED_SIZE, red,
            EGL10.EGL_GREEN_SIZE, green,
            EGL10.EGL_BLUE_SIZE, blue,
            EGL10.EGL_ALPHA_SIZE, alpha,
            EGL10.EGL_DEPTH_SIZE, depth,
            EGL10.EGL_RENDERABLE_TYPE, renderType,
            EGL10.EGL_NONE};
        //获取display
        mEGL10 = (EGL10) EGLContext.getEGL();
        mEGLDisplay = mEGL10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        int [] version = new int[2]; // 主版本 副版本号
        mEGL10.eglInitialize(mEGLDisplay, version);
        // 选择Config
        int[] configNum = new int[1];
        mEGL10.eglChooseConfig(mEGLDisplay, attributes, null, 0, configNum);
        if (configNum[0] == 0) {
            return GLError.ConfigErr;
        }
        EGLConfig[] c = new EGLConfig[configNum[0]];
        mEGL10.eglChooseConfig(mEGLDisplay, attributes, c, configNum[0], configNum);
        mEGLConfig = c[0];
        //创建surface
        int[] surAttr = new int[] {
          EGL10.EGL_WIDTH, width, 
          EGL10.EGL_HEIGHT, height,
          EGL10.EGL_NONE      
        };
        mEGLSurface = createSurface(surAttr);
        //创建context
        int [] contextAttr = new int[]{
                EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        mEGLContext = mEGL10.eglCreateContext(mEGLDisplay, mEGLConfig, shareContext, contextAttr);
        makeCurrent();
        return GLError.OK;
    }

    private void makeCurrent() {
        mEGL10.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
        mGL10 = (GL10) mEGLContext.getGL();
    }

    private EGLSurface createSurface(int[] surAttr) {
        switch (mSurfaceType) {
            case SURFACE_WINDOW:
                return mEGL10.eglCreateWindowSurface(mEGLDisplay, mEGLConfig, mSurfaceNativeObj, surAttr);
            case SURFACE_PIM:
                return mEGL10.eglCreatePixmapSurface(mEGLDisplay, mEGLConfig, mSurfaceNativeObj, surAttr);
            default:
                return mEGL10.eglCreatePbufferSurface(mEGLDisplay, mEGLConfig, surAttr);
        }
    }

    public void destroy() {
        mEGL10.eglMakeCurrent(mEGLDisplay, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE, mEGLContext);
        mEGL10.eglDestroySurface(mEGLDisplay, mEGLSurface);
        mEGL10.eglDestroyContext(mEGLDisplay, mEGLContext);
        mEGL10.eglTerminate(mEGLDisplay);
    }

}
