package com.ray.reopengles.texture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.ray.reopengles.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-10-25 10:50
 *  description : 
 */
public class SimpleTexture implements GLSurfaceView.Renderer {

    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 vPosition;\n"
                    + "attribute vec2 vCoordinate;\n"
                    + "uniform mat4 uMVPMatrix;\n"
                    + "varying vec2 aCoordinate;\n"
                    + "void main() {\n"
                    + "  gl_Position = uMVPMatrix * vPosition;\n"
                    + "  aCoordinate = vCoordinate;\n"
                    + "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;\n"
                    + "uniform sampler2D vTexture;\n"
                    + "varying vec2 aCoordinate;\n"
                    + "void main() {\n"
                    + "   gl_FragColor=texture2D(vTexture,aCoordinate);\n"
                    + "}";

    private static final float[] VERTEX_COORDS = {
            -1f, 1f,
            -1f, -1f,
            1f, 1f,
            1f, -1f
    };

    private static final float[] TEXTURE_COORS = {
            0f, 0f,
            0f, 1f,
            1f, 0f,
            1f, 1f
    };

    private int mProgram;
    private int mMatrixHandle;
    private int mGlHTexture;
    private float[] mMVPMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private int vPositionHandle;
    private int vCoordinateHandle;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordsBuffer;

    public SimpleTexture(Resources resources) {
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.fengj);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        vertexBuffer = ByteBuffer
                .allocateDirect(4 * VERTEX_COORDS.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX_COORDS);
        vertexBuffer.position(0);
        textureCoordsBuffer = ByteBuffer
                .allocateDirect(4 * TEXTURE_COORS.length)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEXTURE_COORS);
        textureCoordsBuffer.position(0);
        //创建程序
        mProgram = glCreateProgram();
        //加载shader
        int vertexShader = loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        glAttachShader(mProgram, vertexShader);
        glAttachShader(mProgram, fragmentShader);
        //链接程序
        glLinkProgram(mProgram);
        //使用程序
        glUseProgram(mProgram);
        vPositionHandle = glGetAttribLocation(mProgram, "vPosition");
        vCoordinateHandle = glGetAttribLocation(mProgram, "vCoordinate");
        mMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
        mGlHTexture = glGetUniformLocation(mProgram, "vTexture");
    }

    private int loadShader(int type, String code) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);
        return shader;
    }

    private Bitmap mBitmap;

    private int createTexture(){
        int [] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            glGenTextures(1, texture, 0);
            //绑定纹理
            glBindTexture(GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
            //通过以上参数生成纹理
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }

        return 0;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        int bW = mBitmap.getWidth();
        int bH = mBitmap.getHeight();
        float sWH = bW*1f/bH;
        float sWidthHeight = width*1f/height;
        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectionMatrix, 0, -sWidthHeight*sWH, sWidthHeight*sWH, -1f, 1f, 3f, 7f);
            } else {
                Matrix.orthoM(mProjectionMatrix, 0, -sWidthHeight/sWH, sWidthHeight/sWH, -1f, 1f, 3f, 7f);
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH, 3f, 7f);
            } else {
                Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -sWH/sWidthHeight, sWH/sWidthHeight, 3f, 7f);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        glUniform1i(mGlHTexture, 0);
        createTexture();
        glEnableVertexAttribArray(vPositionHandle);
        glVertexAttribPointer(vPositionHandle, 2, GL_FLOAT, false, 8, vertexBuffer);
        glEnableVertexAttribArray(vCoordinateHandle);
        glVertexAttribPointer(vCoordinateHandle, 2, GL_FLOAT, false, 8, textureCoordsBuffer);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, VERTEX_COORDS.length/2);
        glDisableVertexAttribArray(vPositionHandle);
        glDisableVertexAttribArray(vCoordinateHandle);
    }

}
