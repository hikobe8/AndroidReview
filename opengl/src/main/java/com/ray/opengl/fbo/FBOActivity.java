package com.ray.opengl.fbo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ray.opengl.R;

import java.nio.ByteBuffer;

public class FBOActivity extends AppCompatActivity implements FBORenderer.Callback {

    private static final String TAG = "FBOActivity";

    private String mImgPath;
    private int mBmpWidth;
    private int mBmpHeight;
    private GLSurfaceView mGLView;
    private FBORenderer mRender;
    private ImageView mImageView;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, FBOActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbo);
        mGLView = findViewById(R.id.gl_view);
        mImageView = findViewById(R.id.img);
        mRender = new FBORenderer(getResources());
        mRender.setCallback(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(mRender);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void clickGallery(View view) {

        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            mImgPath = c.getString(columnIndex);
            Log.e(TAG,"img->"+mImgPath);
            Bitmap bmp= BitmapFactory.decodeFile(mImgPath);
            mBmpWidth=bmp.getWidth();
            mBmpHeight=bmp.getHeight();
            mRender.setBitmap(bmp);
            mGLView.requestRender();
            c.close();
        }
    }

    @Override
    public void onCall(final ByteBuffer data) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Log.d(TAG, "process succeed!");
                final Bitmap bitmap = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
                data.clear();
            }
        }.start();
    }
}
