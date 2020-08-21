package com.hello.animationdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var end = false //动画是否结束
    var revert = false //恢复

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAnimator()
        cameraView.setOnClickListener {
            if (end) {
                startAnimator()
            }
        }

    }

    private fun startAnimator() {
        var topValuesHolder: PropertyValuesHolder
        var bottomValuesHolder: PropertyValuesHolder
        var middleValuesHolder: PropertyValuesHolder
        if (revert) {
            topValuesHolder = PropertyValuesHolder.ofFloat("rotationTopCamera", -45f, 0f)
            bottomValuesHolder = PropertyValuesHolder.ofFloat("rotationBottomCamera", 45f, 0f)
            middleValuesHolder = PropertyValuesHolder.ofFloat("rotationMiddle", 270f, 0f)
        } else {
            topValuesHolder = PropertyValuesHolder.ofFloat("rotationTopCamera", 0f, -45f)
            bottomValuesHolder = PropertyValuesHolder.ofFloat("rotationBottomCamera", 0f, 45f)
            middleValuesHolder = PropertyValuesHolder.ofFloat("rotationMiddle", 0f, 270f)
        }
        ObjectAnimator.ofPropertyValuesHolder(cameraView, bottomValuesHolder, topValuesHolder, middleValuesHolder)
                .apply {
                    startDelay = 1000L
                    duration = 1000L
                    start()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            end = true
                            revert = revert.not()
                        }
                    })
                }
    }
}
