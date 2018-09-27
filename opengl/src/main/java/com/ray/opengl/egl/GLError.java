package com.ray.opengl.egl;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-27 11:43
 *  description : 
 */
public enum GLError {
    OK(0,"ok"),
    ConfigErr(101,"config not support");
    int code;
    String msg;
    GLError(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int value(){
        return code;
    }

    @Override
    public String toString() {
        return msg;
    }
}
