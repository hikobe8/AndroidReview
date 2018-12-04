package com.ray.customizedview.learn_styles

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.ray.customizedview.R

class CustomizeToolBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this)

    }
}