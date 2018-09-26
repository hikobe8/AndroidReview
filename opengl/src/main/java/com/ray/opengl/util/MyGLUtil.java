package com.ray.opengl.util;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-20 14:31
 *  description : 加载shader
 */
public class MyGLUtil {

    private static final String TAG = "ShaderHelper";

    /**
     * 从assets中加载shader代码到内存
     * @param context 上下文环境用于获取 {@link android.content.res.AssetManager}
     * @param fileName shader代码文件的绝对路径
     * @return shader代码字符串
     */
    public static String loadShaderCode(Context context, String fileName){
        StringBuilder code = new StringBuilder();
        try {
            InputStream inputStream = context.getApplicationContext().getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                code.append(line);
                code.append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "can't load shader code : " + fileName);
            e.printStackTrace();
        }
        return code.toString();
    }

    /**
     * 创建Shader句柄
     * @param type shader 类型 GL_VERTEX_SHADER 顶点着色器 / GL_FRAGMENT_SHADER 片元着色器
     * @param shaderCode shader代码字符串
     * @return 编译好的shader句柄
     */
    public static int createShader(int type, String shaderCode) {
        int shader = glCreateShader(type);
        glShaderSource(shader, shaderCode);
        glCompileShader(shader);
        return shader;
    }

    /**
     * 获取已链接的Program 句柄
     * @param vertexCode 顶点着色器代码字符串
     * @param fragmentCode 片元着色器代码字符串
     * @return 已链接的Program 句柄
     */
    public static int getLinkedOpenGLESProgram(String vertexCode, String fragmentCode){
        int vertexShader = createShader(GL_VERTEX_SHADER, vertexCode);
        int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentCode);
        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        return program;
    }

    public static FloatBuffer createFloatBuffer(float[] arr) {
        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(arr.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(arr);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static void getShowMatrix(float[] showMat, int imgWidth, int imgHeight, int viewWidth, int viewHeight){
        if (showMat != null && imgWidth > 0
                && imgHeight > 0 && viewWidth > 0 && viewHeight > 0) {
            float imageRatio = imgWidth * 1f / imgHeight;
            float viewRatio = viewWidth * 1f / viewHeight;
            float[] projection=new float[16];
            float[] camera=new float[16];
            if (imageRatio > viewRatio) {
                Matrix.orthoM(projection, 0, -viewRatio/imageRatio, viewRatio/imageRatio, -1, 1, 1, 3);
            } else {
                Matrix.orthoM(projection, 0, -1, 1, -imageRatio/viewRatio, imageRatio/viewRatio, 1, 3);
            }
            Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
            Matrix.multiplyMM(showMat, 0, projection,0, camera, 0);
        }
    }

    public static float[] rotate(float[] m,float angle){
        Matrix.rotateM(m,0,angle,0,0,1);
        return m;
    }

    public static float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

    public static float[] getOriginalMatrix(){
        return new float[]{
                1,0,0,0,
                0,1,0,0,
                0,0,1,0,
                0,0,0,1
        };
    }

}
