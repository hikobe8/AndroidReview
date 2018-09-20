package com.ray.opengl;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ray.opengl.render.texture.SimpleTextureShader;

public class TextureActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, TextureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_graph);
        GLSurfaceView glSurfaceView = findViewById(R.id.surface);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new SimpleTextureShader(this));
    }
}
