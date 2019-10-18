package com.palmax.arcfacetest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.arcsoft.face.ErrorInfo
import com.palmax.arcfacelib.ArcFaceManager
import com.palmax.arcfacelib.FaceRepository
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    private val NEEDED_PERMISSIONS = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val ACTION_REQUEST_PERMISSIONS = 0x001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS)
        } else {
            ArcFaceManager.init(this)
        }
    }

    /**
     * 激活引擎
     *
     */
    fun activeEngine(v:View) {
        Observable.create(ObservableOnSubscribe<Int> {
            it.onNext(FaceRepository.activeEngine(this))
            it.onComplete()
        }).subscribe(
                object : Observer<Int> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(activeCode: Int) {
                        when (activeCode) {
                            ErrorInfo.MOK -> showToast(getString(R.string.active_success))
                            ErrorInfo.MERR_ASF_ALREADY_ACTIVATED -> showToast(getString(R.string.already_activated))
                            else -> showToast(getString(R.string.active_failed, activeCode))
                        }
                    }

                    override fun onError(e: Throwable) {
                        showToast("active error")
                    }

                    override fun onComplete() {

                    }
                }
        )

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            var isAllGranted = true
            for (grantResult in grantResults) {
                isAllGranted = isAllGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (isAllGranted) {
                ArcFaceManager.init(this)
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }
    }

    private fun checkPermissions(neededPermissions: Array<String>?): Boolean {
        if (neededPermissions == null || neededPermissions.isEmpty()) {
            return true
        }
        var allGranted = true
        for (neededPermission in neededPermissions) {
            allGranted = allGranted and (ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED)
        }
        return allGranted
    }

    private fun showToast(s: String) {
       Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

}
