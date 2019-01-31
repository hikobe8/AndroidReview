package com.palmax.arcfacelib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2019-01-31 10:22
 *  description : 
 */
public class ArcFaceManager {

    /**
     * 初始化location，主要做一些拷贝.so的操作
     */
    @SuppressLint("UnsafeDynamicallyLoadedCode")
    public static void init(Context context) {
        File soFile1;
        soFile1 = new File(context.getDir("jniLibs", Context.MODE_PRIVATE), "libarcsoft_face_engine.so");
        File soFile2;
        soFile2 = new File(context.getDir("jniLibs", Context.MODE_PRIVATE), "libarcsoft_face.so");
        if (soFile1.exists()) {
            System.load(soFile1.getAbsolutePath());
        } else if (soFile2.exists()) {
            System.load(soFile2.getAbsolutePath());
        } else {
            copySoFileToLocal(context, soFile1, soFile2);
        }
    }

    /**
     * 拷贝.so文件 * *
     *
     * @param soFile
     */
    private static void copySoFileToLocal(final Context context, final File... soFile) {
        new Thread(new Runnable() {
            @SuppressLint("UnsafeDynamicallyLoadedCode")
            @Override
            public void run() {
                try {
                    for (File file : soFile) {
                        InputStream inputStream = context.getResources().getAssets().open(file.getName());
                        FileOutputStream fos = new FileOutputStream(file);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            baos.write(buffer, 0, len);
                        }
                        // 从内存到写入到具体文件
                        fos.write(baos.toByteArray());
                        // 关闭文件流
                        inputStream.close();
                        baos.close();
                        fos.close();
                        System.load(file.getAbsolutePath());
                        Log.d("test", "had load " + file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("test","error");
                }
            }
        }).start();
    }
}

