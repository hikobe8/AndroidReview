package com.ray.opengl.render;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-17 18:23
 *  description : 
 */
public class SimpleTriangle extends Shape {
    @Override
    public float[] getCoordinatesArray() {
        return triangleCoords;
    }

    @Override
    public String getVertexShaderCodeString() {
        return sVertexString;
    }

    @Override
    public String getFragmentShaderCodeString() {
        return sFragmentString;
    }
}
