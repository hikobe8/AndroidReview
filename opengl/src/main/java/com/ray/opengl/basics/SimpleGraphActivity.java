package com.ray.opengl.basics;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ray.opengl.R;
import com.ray.opengl.render.geometry.Shape;

public class SimpleGraphActivity extends AppCompatActivity {

    public static void launch(Context context, Class<? extends GLSurfaceView.Renderer> renderer) {
        Intent intent = new Intent(context, SimpleGraphActivity.class);
        intent.putExtra("data", renderer);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_graph);
        GLSurfaceView glSurfaceView = findViewById(R.id.surface);
        glSurfaceView.setEGLContextClientVersion(2);
        Class<? extends GLSurfaceView.Renderer> rendererCls = (Class<? extends Shape>) getIntent().getSerializableExtra("data");
        try {
            glSurfaceView.setRenderer(rendererCls.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
