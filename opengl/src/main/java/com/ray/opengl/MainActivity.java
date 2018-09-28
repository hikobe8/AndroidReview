package com.ray.opengl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ray.opengl.basics.SimpleGraphActivity;
import com.ray.opengl.camera.CameraPreviewActivity;
import com.ray.opengl.camera.sticker.StickerCameraActivity;
import com.ray.opengl.egl.EGLActivity;
import com.ray.opengl.fbo.FBOActivity;
import com.ray.opengl.render.texture.SimpleTextureShader;

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

    public void cameraCLick(View view) {
        CameraPreviewActivity.launch(this);
    }

    public void fboCLick(View view) {
        FBOActivity.launch(this);
    }

    public void eglCLick(View view) {
        EGLActivity.launch(this);
    }

    public void stickerClick(View view) {
        StickerCameraActivity.launch(this);
    }
}
