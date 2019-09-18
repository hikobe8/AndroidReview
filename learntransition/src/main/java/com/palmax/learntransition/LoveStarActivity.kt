package com.palmax.learntransition

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_love_star.*

class LoveStarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_love_star)
        heart.setAnimationListener {
            text.alpha = it
        }
    }
}
