package com.ray.camera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ray.opengl.R;
import com.ray.opengl.camera.CameraPreviewWithGLActivity;

public class CameraOptionActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, CameraOptionActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_option);
    }

    public void cameraWithGLClick(View view) {
        CameraPreviewWithGLActivity.launch(this);
    }

    public void cameraClick(View view) {

    }
}
