package com.x.coroutine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("线程名 -----" + Thread.currentThread().name)
        GlobalScope.launch(Dispatchers.IO) {
            println("线程名 -----" + Thread.currentThread().name)
        }
        GlobalScope.launch(Dispatchers.Main) {

            val bitmap = getBitmap()

            image.setImageBitmap(bitmap)

        }
        //切割图片
        val coroutineScope = CoroutineScope(EmptyCoroutineContext)
        coroutineScope.launch(Dispatchers.Main) {
            val bitmap = getBitmap()
            val first = cropImage(bitmap, 4, 0, 0)
            val last = cropImage(bitmap, 9, 2, 2)
            image1.setImageBitmap(first)
            image2.setImageBitmap(last)
        }

    }

    /**
     * 裁剪图片
     * size 份数
     * index 索引值
     */
    private suspend fun cropImage(bitmap: Bitmap, size: Int, rowIndex: Int, colIndex: Int) = withContext(Dispatchers.IO) {
        val sqrt = sqrt(size.toDouble())
        if (sqrt.pow(2.0) != size.toDouble())
            throw RuntimeException("wrong size!")
        val dstWidth = (bitmap.width / sqrt * 1f).toInt()
        val dstHeight = (bitmap.height / sqrt * 1f).toInt()
        Bitmap.createBitmap(bitmap, dstWidth * colIndex, dstHeight * rowIndex, dstWidth, dstHeight, null, false)
    }

    private suspend fun getBitmap(): Bitmap = withContext(Dispatchers.IO) {
        URL("http://image.biaobaiju.com/uploads/20180530/01/1527614596-ofYvEheDMX.jpg").run {
            val openConnection = openConnection() as HttpURLConnection
            val inputStream = openConnection.inputStream
            BitmapFactory.decodeStream(inputStream)
        }
    }

}
