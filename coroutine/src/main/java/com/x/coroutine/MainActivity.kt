package com.x.coroutine

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("线程名 -----" + Thread.currentThread().name)
        GlobalScope.launch(Dispatchers.IO) {
            println("线程名 -----" + Thread.currentThread().name)
        }
        GlobalScope.launch(Dispatchers.Main) {

            val bitmap = withContext(Dispatchers.IO) {
                URL("http://image.biaobaiju.com/uploads/20180530/01/1527614596-ofYvEheDMX.jpg").run {
                    val openConnection = openConnection() as HttpURLConnection
                    val inputStream = openConnection.inputStream
                    BitmapFactory.decodeStream(inputStream)
                }
            }

            image.setImageBitmap(bitmap)

        }
    }
}
