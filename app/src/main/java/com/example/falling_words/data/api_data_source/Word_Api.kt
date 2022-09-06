package com.example.falling_words.data.api_data_source

import com.example.falling_words.model.Words
import retrofit2.Response

interface Word_Api {
     suspend fun getwords(): Response<Words>
}