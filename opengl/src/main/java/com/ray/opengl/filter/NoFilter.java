package com.ray.opengl.filter;

import android.content.res.Resources;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-28 18:03
 *  description : 
 */
public class NoFilter extends AFilter {

    public NoFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/base_vertex.sh",
                "shader/base_fragment.sh");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
