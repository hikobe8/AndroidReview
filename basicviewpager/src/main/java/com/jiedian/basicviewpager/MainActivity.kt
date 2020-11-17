package com.jiedian.basicviewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.view1).setOnClickListener {
            Toast.makeText(this, "view1 clicked", Toast.LENGTH_SHORT).show()
        }
    }
}