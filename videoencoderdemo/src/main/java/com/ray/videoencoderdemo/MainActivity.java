package com.ray.videoencoderdemo;

import android.opengl.GLES20;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ray.videoencoderdemo.opengl.EglHelper;
import com.ray.videoencoderdemo.opengl.MyGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGLSurfaceView surfaceView = new MyGLSurfaceView(this);
        setContentView(surfaceView);
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//
//            }
//
//            @Override
//            public void surfaceChanged(final SurfaceHolder holder, int format, final int width, final int height) {
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        EglHelper eglHelper = new EglHelper();
//                        eglHelper.initEgl(holder.getSurface(), null);
//                        while (true) {
//                            GLES20.glViewport(0, 0, width, height);
//                            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//                            GLES20.glClearColor(1.0f, 0f, 0f, 1f);
//                            eglHelper.swapBuffers();
//                            try {
//                                Thread.sleep(16);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }.start();
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });
    }
}