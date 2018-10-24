package com.ray.reopengles.simplegraph;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ray.reopengles.R;

public class SimpleGraphActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, SimpleGraphActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGLSurfaceView = findViewById(R.id.gl_surface);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(createRendererByType());
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGLSurfaceView.requestRender();
    }

    private GLSurfaceView.Renderer createRendererByType() {
        int type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 1:
                return new Square();
            default:
                return new Triangle();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

}
