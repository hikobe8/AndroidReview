package com.palmax.commonutil

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2019-01-23 17:09
 *  description :
 */

object ImagePicker {

    fun choosePhoto(activity: Activity, reqCode: Int = 0) {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
        activity.startActivityForResult(intentToPickPic, reqCode)
    }

}