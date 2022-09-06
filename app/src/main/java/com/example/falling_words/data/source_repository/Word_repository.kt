package com.example.falling_words.data.source_repository
import com.example.falling_words.data.api_data_source.Word_Api
import com.example.falling_words.model.Words
import retrofit2.Response

class Word_repository(private val wordApi: Word_Api) {

    suspend fun getWords(): Response<Words> {
     return  wordApi.getwords()
    }
    }