package com.example.falling_words.data.retrofit

import com.example.falling_words.data.api_data_source.Word_Api
import com.example.falling_words.model.Words
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitApi:Word_Api {
    @GET("/DroidCoder/7ac6cdb4bf5e032f4c737aaafe659b33/raw/baa9fe0d586082d85db71f346e2b039c580c5804/words.json")
    override suspend fun getwords(): Response<Words>
    companion object {
        var retrofitService: RetrofitApi? = null
        fun getInstance() : RetrofitApi {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://gist.githubusercontent.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitApi::class.java)
            }
            return retrofitService!!
        }

    }
}