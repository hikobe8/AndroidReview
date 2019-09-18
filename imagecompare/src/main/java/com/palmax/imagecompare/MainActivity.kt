package com.palmax.imagecompare

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.palmax.commonutil.HistogramSimilarUtil
import com.palmax.commonutil.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                initView()
            }
        } else {
            initView()
        }
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        iv_1.setOnClickListener {
            ImagePicker.choosePhoto(this, 1)
        }
        iv_2.setOnClickListener {
            ImagePicker.choosePhoto(this, 2)
        }
    }

    private var mBmp1:Bitmap?=null
    private var mBmp2:Bitmap?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 获取图片
        try {
            //该uri是上一个Activity返回的
            val imageUri = data?.data
            if(imageUri!=null) {
                val bit = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                if (requestCode == 1) {
                    mBmp1 = bit
                    checkAndStartCompare()
                    iv_1.setImageBitmap(bit)
                } else if (requestCode == 2) {
                    mBmp2 = bit
                    checkAndStartCompare()
                    iv_2.setImageBitmap(bit)
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun checkAndStartCompare() {

        if (mBmp1 != null && mBmp2 != null) {

            tv_result.text = "开始对比"
            Thread(Runnable {

                val histogram1 = HistogramSimilarUtil.getHistogram(mBmp1)
                val histogram2 = HistogramSimilarUtil.getHistogram(mBmp2)

                val value = HistogramSimilarUtil.indentification(histogram1, histogram2)
                runOnUiThread {

                    tv_result.text = "相似度 = $value"

                }

            }).start()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            initView()
        }
    }

}
