package com.ray.camera;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SimpleCameraActivity extends AppCompatActivity {

    SimpleCameraView mSimpleCameraView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, SimpleCameraActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSimpleCameraView = new SimpleCameraView(this);
        setContentView(mSimpleCameraView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimpleCameraView.releasePreview();
    }
}
