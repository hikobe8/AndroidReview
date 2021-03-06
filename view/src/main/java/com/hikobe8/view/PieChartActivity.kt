package com.hikobe8.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PieChartActivity : AppCompatActivity() {

    companion object{
        fun launch(context: Context) {
            val intent = Intent(context, PieChartActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)
    }
}
