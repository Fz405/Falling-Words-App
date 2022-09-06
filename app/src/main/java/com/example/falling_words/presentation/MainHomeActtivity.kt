package com.example.falling_words.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.falling_words.viewmodel.MainMovieViwModel
import com.example.falling_words.databinding.ActivityMainHomeActtivityBinding

class MainHomeActtivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainHomeActtivityBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= ActivityMainHomeActtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.learnEng.setOnClickListener {
            Learn_lang(true)
        }
        binding.learnSpa.setOnClickListener {
            Learn_lang(false)
        }

            }

    private fun Learn_lang(toLearnSpanish: Boolean) {
        val intent = Intent(this@MainHomeActtivity, MainActivity::class.java)
        intent.putExtra(MainMovieViwModel.ARG_LEARN_ENG, !toLearnSpanish)
        startActivity(intent)
    }
}