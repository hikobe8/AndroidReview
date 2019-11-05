package com.hikobe8.learnretrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Author : Ray
 * Time : 2019-10-18 17:15
 * Description :
 */
interface GitHubService {
    @GET("users/{user}")
    fun user(@Path("user") user: String): Call<ResponseBody>
}
