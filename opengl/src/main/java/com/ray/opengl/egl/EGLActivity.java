package com.ray.opengl.egl;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ray.opengl.R;
import com.ray.opengl.filter.GrayFilter;

public class EGLActivity extends AppCompatActivity {

    private ImageView mImage;

    private int mBmpWidth,mBmpHeight;
    private GLES20BackEnv mBackEnv;

    public static void launch(Context context) {
        Intent intent = new Intent(context, EGLActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egl);
        mImage= (ImageView)findViewById(R.id.mImage);
    }

    public void onClick(View view) {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                String imgPath = cursor.getString(columnIndex);
//                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                mBackEnv = new GLES20BackEnv(width, height);
                mBackEnv.setThreadOwner(getMainLooper().getThread().getName());
                mBackEnv.setFilter(new GrayFilter(getResources()));
                mBackEnv.setInput(bitmap);
                Bitmap backEnvBitmap = mBackEnv.getBitmap();
                mImage.setImageBitmap(backEnvBitmap);
                cursor.close();
            }

        }
    }
}

