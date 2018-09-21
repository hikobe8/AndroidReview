package com.ray.opengl;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ray.opengl.render.texture.Filter;
import com.ray.opengl.render.texture.SimpleTextureShader;

public class TextureActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private SimpleTextureShader mSimpleTextureShader;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TextureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_graph);
        mGLSurfaceView = findViewById(R.id.surface);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mSimpleTextureShader = new SimpleTextureShader(this);
        mGLSurfaceView.setRenderer(mSimpleTextureShader);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_process, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gray:
                //黑白
                mSimpleTextureShader.setFilter(new Filter(1, new float[]{0.299f, 0.587f, 0.114f}));
                break;
            case R.id.mCool:
                mSimpleTextureShader.setFilter(new Filter(2, new float[]{0f, 0f, 0.1f}));
                break;
            case R.id.mWarm:
                mSimpleTextureShader.setFilter(new Filter(2, new float[]{0.1f, 0.1f, 0f}));
                break;
            case R.id.mBlur:
                mSimpleTextureShader.setFilter(new Filter(3, new float[]{0.006f, 0.004f, 0.002f}));
                break;
            case R.id.mMagn:
                mSimpleTextureShader.setFilter(new Filter(4, new float[]{0.0f, 0.0f, 0.4f}));
                break;
            default:
                //原图
                mSimpleTextureShader.setFilter(new Filter(0, new float[]{0f, 0f, 0f}));
                break;
        }
        mGLSurfaceView.requestRender();
        return true;
    }
}
