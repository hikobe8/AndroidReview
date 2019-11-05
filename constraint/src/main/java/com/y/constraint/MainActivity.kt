package com.y.constraint

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.ViewAnimationUtils
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 15000L
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                earth.layoutParams = (earth.layoutParams as ConstraintLayout.LayoutParams).apply {
                    circleAngle = it.animatedValue as Float
                }
            }
            interpolator = LinearInterpolator()
            start()
        }
        ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 5000L
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                moon.layoutParams = (moon.layoutParams as ConstraintLayout.LayoutParams).apply {
                    circleAngle = it.animatedValue as Float
                }
            }
            interpolator = LinearInterpolator()
            start()
        }
    }
}
