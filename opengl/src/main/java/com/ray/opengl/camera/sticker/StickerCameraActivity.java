package com.ray.opengl.camera.sticker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.ray.opengl.R;
import java.io.IOException;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class StickerCameraActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Renderer mRenderer;
    private int cameraId = 1;
    private TextureController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
                return;
            }
        }
        setContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mController != null) {
            mController.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mController != null) {
            mController.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mController != null) {
            mController.onDestroy();
        }
    }

    private void setContent() {
        setContentView(R.layout.activity_sticker_camera);
        mSurfaceView = findViewById(R.id.mSurface);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mController.surfaceCreated(holder);
                mController.setRenderer(mRenderer);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mController.surfaceChanged(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mController.surfaceDestroyed();
            }
        });
    }

    private class Camera1Renderer implements Renderer {

        private Camera mCamera;

        @Override
        public void onDestroy() {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
            mCamera = Camera.open(cameraId);
            mController.setImageDirection(cameraId);
            Camera.Size size = mCamera.getParameters().getPreviewSize();
            mController.setDataSize(size.height, size.width);
            try {
                mCamera.setPreviewTexture(mController.getTexture());
                mController.getTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        mController.requestRender();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0) {
            setContent();
        }
    }
}
