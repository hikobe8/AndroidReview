package com.ray.opengl.basics;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ray.opengl.R;
import com.ray.opengl.render.SimpleTriangle;

public class SimpleGraphActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SimpleGraphActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_graph);
        GLSurfaceView glSurfaceView = findViewById(R.id.surface);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new SimpleTriangle());
    }
}
