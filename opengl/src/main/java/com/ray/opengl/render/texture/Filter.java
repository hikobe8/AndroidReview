package com.ray.opengl.render.texture;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-21 16:28
 *  description : 
 */
public class Filter {

    public int mType;

    public float[] changeColor;

    public Filter(int type, float[] changeColor) {
        mType = type;
        this.changeColor = changeColor;
    }
}
