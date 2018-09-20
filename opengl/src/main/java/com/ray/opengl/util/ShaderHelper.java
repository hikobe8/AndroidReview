package com.ray.opengl.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
public class ShaderHelper {

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
        int fragmentShader = createShader(GL_VERTEX_SHADER, fragmentCode);
        int program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        return program;
    }


}
