package com.ray.opengl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ray.camera.CameraOptionActivity;
import com.ray.opengl.camera.sticker.StickerCameraActivity;
import com.ray.opengl.egl.EGLActivity;
import com.ray.opengl.fbo.FBOActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basicDrawClick(View view) {
        BasicOptionActivity.launch(this);
    }

    public void textureClick(View view) {
        TextureActivity.launch(this);
    }

    public void cameraClick(View view) {
        CameraOptionActivity.launch(this);
    }

    public void fboClick(View view) {
        FBOActivity.launch(this);
    }

    public void eglClick(View view) {
        EGLActivity.launch(this);
    }

    public void stickerClick(View view) {
        StickerCameraActivity.launch(this);
    }
}
