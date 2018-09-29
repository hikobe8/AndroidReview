package com.ray.opengl.camera.sticker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ray.opengl.camera.TextureFilter;
import com.ray.opengl.filter.AFilter;
import com.ray.opengl.filter.NoFilter;
import com.ray.opengl.util.EasyGlUtils;
import com.ray.opengl.util.MatrixUtils;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

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
    //中间特效
    private GroupFilter mGroupFilter;
    //特效处理的Filter
    private TextureFilter mEffectFilter;
    //数据的尺寸
    private Point mDataSize;
    //输出视图的大小
    private Point mWindowSize;

    private AtomicBoolean isParamSet = new AtomicBoolean(false);

    //创建离屏幕buffer,用于最后导出数据
    private int[] mExportFrame = new int[1];
    private int[] mExportTexture = new int[1];
    //绘制到屏幕上的变换矩阵
    private float[] mShowMatrix = new float[16];
    //绘制回调缩放的矩阵
    private float[] mCallbackMatrix = new float[16];
    //回调数据的宽高
    private int mFrameCallbackWidth, mFrameCallbackHeight;
    private int mDirectionFlag = -1;
    //输出到屏幕的缩放类型
    private int mShowType = MatrixUtils.TYPE_CENTERCROP;
    //录像flag
    private boolean mIsRecord;
    //拍照flag
    private boolean mIsShoot;
    //用于存储回调数据的buffer
    private ByteBuffer[] mOutputBuffer = new ByteBuffer[3];
    //数据回调
    private FrameCallback mFrameCallback;
    //回调数据使用的buffer索引
    private int mIndexOutput;

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
        mEffectFilter = new TextureFilter(mContext.getResources());
        mShowFilter = new NoFilter(mContext.getResources());
        mGroupFilter = new GroupFilter(mContext.getResources());
        //设置默认的DataSize
        mDataSize = new Point(720, 1280);
        mWindowSize = new Point(720, 1280);
        mGLView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mGLView.attachedToWindow();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mGLView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mGLView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mEffectFilter.create();
        mGroupFilter.create();
        mShowFilter.create();
        if (!isParamSet.get()) {
            if (mRenderer != null) {
                mRenderer.onSurfaceCreated(gl, config);
            }
            setParamSet();
        }
        calculateCallbackOM();
        mEffectFilter.setFlag(mDirectionFlag);

        deleteFrameBuffer();
        GLES20.glGenFramebuffers(1, mExportFrame, 0);
        EasyGlUtils.genTexturesWithParameter(1, mExportTexture, 0, GLES20.GL_RGBA, mDataSize.x, mDataSize.y);
    }

    private void deleteFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, mExportFrame, 0);
        GLES20.glDeleteTextures(1, mExportFrame, 0);
    }

    private void calculateCallbackOM() {
        if (mFrameCallbackHeight > 0 && mFrameCallbackWidth > 0 && mDataSize.x > 0 && mDataSize.y > 0) {
            //计算输出的变换矩阵
            MatrixUtils.getMatrix(mCallbackMatrix, MatrixUtils.TYPE_CENTERCROP, mDataSize.x, mDataSize.y,
                    mFrameCallbackWidth,
                    mFrameCallbackHeight);
            MatrixUtils.flip(mCallbackMatrix, false, true);
        }
    }

    private void setParamSet() {
        if (!isParamSet.get() && mDataSize.x > 0 && mDataSize.y > 0) {
            isParamSet.set(true);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        MatrixUtils.getMatrix(mShowMatrix, mShowType, mDataSize.x, mDataSize.y,
                width, height);
        mShowFilter.setSize(width, height);
        mShowFilter.setMatrix(mShowMatrix);
        mGroupFilter.setSize(mDataSize.x, mDataSize.y);
        mEffectFilter.setSize(mDataSize.x, mDataSize.y);
        mShowFilter.setSize(mDataSize.x, mDataSize.y);
        if (mRenderer != null) {
            mRenderer.onSurfaceChanged(gl, width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (isParamSet.get()) {
            mEffectFilter.draw();
            mGroupFilter.setTextureId(mEffectFilter.getOutputTexture());
            mGroupFilter.draw();
            //显示传入的texture上，一般为显示在屏幕上
            GLES20.glViewport(0, 0,mWindowSize.x, mWindowSize.y);
            mShowFilter.setMatrix(mShowMatrix);
            mShowFilter.setTextureId(mGroupFilter.getOutputTexture());
            mShowFilter.draw();
            if (mRenderer != null) {
                mRenderer.onDrawFrame(gl);
            }
        }
        callbackFrameIfNeed();
    }

    public void setFrameCallback(int width, int height, FrameCallback frameCallback) {
        mFrameCallbackWidth = width;
        mFrameCallbackHeight = height;
        if (mFrameCallbackWidth > 0 && mFrameCallbackHeight > 0) {
            if (mOutputBuffer != null) {
                mOutputBuffer = new ByteBuffer[3];
            }
            mFrameCallback = frameCallback;
        } else {
            mFrameCallback = null;
        }
    }

    //缩放图片到制定大小，读取数据并回调给调用端
    private void callbackFrameIfNeed() {
        if (mFrameCallback != null && (mIsRecord || mIsShoot)) {
            mIndexOutput = mIndexOutput ++ >= 2 ? 0 : mIndexOutput;
            if (mOutputBuffer[mIndexOutput] == null) {
                mOutputBuffer[mIndexOutput] = ByteBuffer.allocate(mFrameCallbackWidth * mFrameCallbackHeight * 4);
            }
            GLES20.glViewport(0,0, mFrameCallbackWidth, mFrameCallbackHeight);
            EasyGlUtils.bindFrameTexture(mExportFrame[0], mExportTexture[0]);
            mShowFilter.setMatrix(mCallbackMatrix);
            mShowFilter.draw();
            frameCallback();
            mIsShoot = false;
            EasyGlUtils.unBindFrameBuffer();
            mShowFilter.setMatrix(mShowMatrix);
        }
    }

    private void frameCallback() {
        GLES20.glReadPixels(0, 0, mFrameCallbackWidth, mFrameCallbackHeight, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, mOutputBuffer[mIndexOutput]);
        mFrameCallback.onFrame(mOutputBuffer[mIndexOutput].array(), 0);
    }

    public void addFilter(AFilter filter) {
        mGroupFilter.addFilter(filter);
    }

    public void setImageDirection(int flag) {
        mDirectionFlag = flag;
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

    public void destroy() {
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
            setEGLWindowSurfaceFactory(new GLSurfaceView.EGLWindowSurfaceFactory() {

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

        public void detachedFromWindow() {
            super.onDetachedFromWindow();
        }

        public void clear() {

        }
    }

}
