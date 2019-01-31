package com.palmax.arcfacelib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import dalvik.system.PathClassLoader;

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
    public static void init(Context context) throws Exception {
        createNewNativeDir(context);
        File soFile1;
        soFile1 = new File(context.getDir("libs", Context.MODE_PRIVATE), "libarcsoft_face.so");
        File soFile2;
//        soFile2 = new File(context.getDir("libs", Context.MODE_PRIVATE), "libarcsoft_face.so");
        if (soFile1.exists()) {
            System.load(soFile1.getAbsolutePath());
        }
//        else if (soFile2.exists()) {
//            System.load(soFile2.getAbsolutePath());
//        }
        else {
            copySoFileToLocal(context, soFile1/*, soFile2*/);
        }
    }

    private static void createNewNativeDir(Context context) throws Exception {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Field declaredField = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
        declaredField.setAccessible(true);
        Object pathList = declaredField.get(pathClassLoader);
        // 获取当前类的属性
        Object nativeLibraryDirectories = pathList.getClass().getDeclaredField(
                "nativeLibraryDirectories");
        ((Field) nativeLibraryDirectories).setAccessible(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 获取 DEXPATHList中的属性值
            File[] files = (File[]) ((Field) nativeLibraryDirectories).get(pathList);
            Object filesss = Array.newInstance(File.class, files.length + 1);
            // 添加自定义.so路径
            Array.set(filesss, 0, context.getDir("libs", Context.MODE_PRIVATE));
            // 将系统自己的追加上
            for (int i = 1; i < files.length + 1; i++) {
                Array.set(filesss, i, files[i - 1]);
            }
            ((Field) nativeLibraryDirectories).set(pathList, filesss);
        } else {
            ArrayList<File> files = (ArrayList<File>) ((Field) nativeLibraryDirectories).get(pathList);
            ArrayList<File> filesss = (ArrayList<File>) files.clone();
            filesss.add(0, context.getDir("libs", Context.MODE_PRIVATE));
            ((Field) nativeLibraryDirectories).set(pathList, filesss);
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

