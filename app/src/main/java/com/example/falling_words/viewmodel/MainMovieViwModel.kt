package com.example.falling_words.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.falling_words.data.retrofit.RetrofitApi
import com.example.falling_words.data.source_repository.Word_repository
import com.example.falling_words.model.Words
import com.example.falling_words.model.WordsItem
import kotlinx.coroutines.*
import kotlin.random.Random

class MainMovieViwModel(application: Application):
    AndroidViewModel(application)
 {
     var index: Int = 0
      val translationIndices: ArrayList<Int> = ArrayList()

     val handler = CoroutineExceptionHandler { _, exception ->
         Log.d(TAG, "$exception handled !")
     }
     private var job: Job?=null
     val wordsList: ArrayList<WordsItem> = ArrayList<WordsItem>()
      private val repository= Word_repository(RetrofitApi.getInstance())


     val errorMessage = MutableLiveData<String>()
     val wordlivedata=MutableLiveData<Words>()
       val loading = MutableLiveData<Boolean>()

     fun getAllWords() {
         job = CoroutineScope(Dispatchers.IO + handler).launch {
             //try {
                 loading.postValue(true)
                 val response = repository.getWords()
             // condradict
                 withContext(Dispatchers.Main) {
                     if (response.isSuccessful) {
                       for(newword in response?.body()!!) {
                         wordlivedata.postValue(response.body())
                          }
                     } else {
                         onError("Error : ${response.message()} ")
                     }
                 }



         }


     }
     private fun onError(message: String) {
         errorMessage.value = message
             }

     override fun onCleared() {
         super.onCleared()
         job?.cancel()
     }
     companion object {
         const val ANSWER_TIME_DURATION: Long = 11000 //seconds for floating translation
         const val GAME_LIFE: Int = 25 // max attempts, includes missing as well
         const val ARG_LEARN_ENG = "learn_english" // which language user wanted to learn
     }



      }