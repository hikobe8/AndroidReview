package com.hello.windowview

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.layout_window.view.*

class MainActivity : AppCompatActivity() {

    lateinit var windowView: MyWindow

    private var windowShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        windowView = LayoutInflater.from(this).inflate(R.layout.layout_window, null) as MyWindow
        windowView.backCallback = {
            onBackPressed()
        }
        windowView.close.setOnClickListener {
            if (windowShowed) {
                windowManager.removeView(windowView)
                windowShowed = false
            }
        }
    }

    fun showWindow(view: View) {
        windowShowed = true
        windowManager.addView(windowView, WindowManager.LayoutParams().apply {
            width = 200f.dp2Px().toInt()
            height = 300f.dp2Px().toInt()
            flags = flags and (WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH.inv())
        })
    }

    override fun onBackPressed() {
        if (windowShowed) {
            windowManager.removeView(windowView)
            windowShowed = false
            return
        }
        super.onBackPressed()
    }
}

class MyWindow(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var backCallback: (() -> Unit)? = null

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            backCallback?.invoke()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

}

fun Float.dp2Px(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}