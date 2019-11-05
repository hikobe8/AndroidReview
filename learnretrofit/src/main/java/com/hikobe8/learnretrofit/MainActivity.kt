package com.hikobe8.learnretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build()

        val service = retrofit.create(GitHubService::class.java)
        btn_github.setOnClickListener {
            val userCall = service.user("hikobe8")
            userCall.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(this@MainActivity, "succeed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
