package com.hikobe8.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun dashboard(view: View) {
        DashboardActivity.launch(this)
    }

    fun piechart(view: View) {
        PieChartActivity.launch(this)
    }

}
