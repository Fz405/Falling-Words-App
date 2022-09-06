package com.example.falling_words.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.falling_words.databinding.ActivityResultBinding

class FinalActivity : AppCompatActivity(){
    private lateinit var binding: ActivityResultBinding;
    private var mCorrectCount: String? = ""
    private var mTotalCount: String? = ""



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.retryBtn.setOnClickListener {
            finish()
        }
        intent?.let {
            mTotalCount = it.getStringExtra(ARG_TOTAL_COUNT)
            mCorrectCount = it.getStringExtra(ARG_CORRECT_COUNT)
        }
       binding.scoreValue.text = "Score $mCorrectCount / $mTotalCount"

    }

    companion object {
        const val ARG_TOTAL_COUNT = "total_count"
        const val ARG_CORRECT_COUNT = "correct_count"
    }
}