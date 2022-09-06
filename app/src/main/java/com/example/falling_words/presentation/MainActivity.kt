package com.example.falling_words.presentation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.falling_words.AppConstants
import com.example.falling_words.MyCountDownTimer
import com.example.falling_words.model.Words
import com.example.falling_words.model.WordsItem
import com.example.falling_words.viewmodel.MainMovieViwModel
import com.example.falling_words.viewmodel.MainMovieViwModel.Companion.ANSWER_TIME_DURATION
import com.example.falling_words.viewmodel.MainMovieViwModel.Companion.ARG_LEARN_ENG
import com.example.falling_words.viewmodel.MainMovieViwModel.Companion.GAME_LIFE
import com.example.falling_words.R
import com.example.falling_words.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class MainActivity: AppCompatActivity(), Animator.AnimatorListener {
    var index: Int = 0
       private var job: Job?=null
    private var unAnsweredCount: Int= 0
    private var correctCount: Int =0
    private var wrongCount: Int =0
    private var toLearnEnglish: Boolean = true
    private lateinit var displayMetrics: DisplayMetrics
    private var objectAnimator: ObjectAnimator? = null
    private var timer: MyCountDownTimer? = null

    val translationIndices: ArrayList<Int> = ArrayList()
    var currentTranslationOption: String? = null
    var currentWord: WordsItem? = null
    val wordsList: ArrayList<WordsItem> = ArrayList<WordsItem>()
    private lateinit var binding: ActivityMainBinding;
    lateinit var viewmodel: MainMovieViwModel
    private val mCounterReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val counterValue = intent.getStringExtra("message")
            updateCounterText(counterValue)
        }
    }

    private fun updateCounterText(counter: String?) {
        counter?.let {
            binding.timerValue.text = it
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewmodel = ViewModelProvider(this)[MainMovieViwModel::class.java]
        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        viewmodel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        binding.correctBtn.setOnClickListener {
            if (isCorrectVisible()) {
                updateCorrectCount()
                setNewWord()
            } else {
                updateWrongCount()
                setNewTranslationWord()
            }
        }

      binding.wrongBtn.setOnClickListener {
        if (isCorrectVisible()) {
            updateWrongCount()
            setNewWord()
        } else {
            updateCorrectCount()
            setNewTranslationWord()
        }
    }

        initViewsState()
        initAnimation()
        initializeTimer()
        setObservers()

        intent?.let {
            toLearnEnglish = it.getBooleanExtra(ARG_LEARN_ENG, true)
        }
       viewmodel.getAllWords()

    }

    private fun initViewsState() {
        binding.parentLayout.visibility = View.GONE
        binding.successCounterLayout.counterText.text = getString(R.string.correctTitle)
        binding.failureCounterLayout.counterText.text = getString(R.string.wrongTitle)
        binding.unAnsweredCounterLayout.counterText.text = getString(R.string.missedTitle)

    }

    fun initializeWords(words: ArrayList<WordsItem>) {
        wordsList?.clear()
        //for (i in 0 until words.size) {
            wordsList?.addAll(ArrayList(words))
       // }
    }

    private fun setNewWord() {
        currentWord = getNextWord()
        if (toLearnEnglish) {
            binding.word.text = currentWord?.text_eng
        } else {
                binding.word.text = currentWord?.text_spa
        }
        setNewTranslationWord()
    }
     fun getNextWord(): WordsItem {
        translationIndices.clear()
        return getRandomUniqueWord(false)
    }
    private fun getRandomUniqueWord(isForTranslation: Boolean): WordsItem {
        job = CoroutineScope(Dispatchers.Default).launch {
            val random = Random(System.currentTimeMillis())
            do {
                index = random.nextInt(0, 15)
            } while (translationIndices.contains(index))

            if (isForTranslation) {
                translationIndices.add(index)
            }
            ensureActive()
            delay(200L)
            job?.cancel()
        }

        return wordsList!![index]

    }
  /*  private fun getRandomUniqueWord(isForTranslation: Boolean): WordsItem {

        val random = Random(System.currentTimeMillis())
        var index: Int
        do {
            index = random.nextInt(0, 15)
        } while (translationIndices.contains(index))

        if (isForTranslation) {
            translationIndices.add(index)
        }

        return wordsList!![index]
    }*/
    private fun setNewTranslationWord() {
        if (toLearnEnglish) {
            currentTranslationOption = getNextTranslation().text_spa
        } else {
           currentTranslationOption = getNextTranslation().text_eng
        }
        binding.translationText.text = currentTranslationOption
        animateTranslationText()
    }
     fun getNextTranslation(): WordsItem {
        return getRandomUniqueWord(true)
    }
    private fun animateTranslationText() {
        startAnimation()
    }

    //starting animation and handle timer
    private fun startAnimation() {
        stopAnimation()
        timer?.cancelTimer()
        timer?.startTimer()
        objectAnimator?.start()
    }

    //stopping animation on textView
    private fun stopAnimation() {
        objectAnimator?.isRunning?.let {
           binding.translationText.clearAnimation()
        }
    }
    private fun setObservers() {
        viewmodel.wordlivedata.observe(this, Observer { words ->
            words?.let {
                binding.parentLayout.visibility = View.VISIBLE
                initializeWords(words)
                setNewWord()
            }
        })
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mCounterReceiver,
            IntentFilter(AppConstants.INTENT_FILTER_ON_TICK)
        )
    }


    //initialize timer first time
    private fun initializeTimer() {
        timer = MyCountDownTimer.getInstance(this)
           }

    //initialize floating animation first time
    private fun initAnimation() {

        objectAnimator = ObjectAnimator.ofFloat(
            binding.translationText,
            "translationY",
            0f,
            displayMetrics.heightPixels.toFloat()
        )

        objectAnimator?.duration = ANSWER_TIME_DURATION
        objectAnimator?.addListener(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
        timer?.pauseTimer()
        objectAnimator?.isRunning.let {
            objectAnimator?.pause()
        }
    }

    //resuming state of timer & animation
    override fun onResume() {
        super.onResume()
        timer?.resumeTimer()
        objectAnimator?.isRunning.let {
            objectAnimator?.resume()
        }
    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
        updateUnAnsweredCount()
        if (isCorrectVisible()) {
            setNewWord()
            return
        }
        setNewTranslationWord()

    }

    fun isCorrectVisible(): Boolean {
        return if (toLearnEnglish) {
            currentWord?.text_spa.equals(currentTranslationOption, ignoreCase = true)
        } else {
            currentWord?.text_eng.equals(currentTranslationOption, ignoreCase = true)
        }
    }
    //updating count
    private fun updateCorrectCount() {
        correctCount += 1
       binding.successCounterLayout.counterValue.text = correctCount.toString()
        checkGameLife()
    }

    //updating count
    private fun updateWrongCount() {
        wrongCount += 1
        binding.failureCounterLayout.counterValue.text = wrongCount.toString()
        checkGameLife()
    }

    //updating count
    private fun updateUnAnsweredCount() {
        unAnsweredCount += 1
        binding.unAnsweredCounterLayout.counterValue.text = unAnsweredCount.toString()
        checkGameLife()
    }

    //checking attempts to end game
    private fun checkGameLife() {
        if (unAnsweredCount + correctCount + wrongCount == GAME_LIFE) {
            gameOver()
        }
    }

    private fun gameOver() {
        startResultActivity()
        finish()
    }

    //showing  results to user & giving option to play again
    private fun startResultActivity() {
        val intent = Intent(this@MainActivity, FinalActivity::class.java)
        intent.putExtra(FinalActivity.ARG_TOTAL_COUNT, GAME_LIFE.toString())
        intent.putExtra(FinalActivity.ARG_CORRECT_COUNT, correctCount.toString())
        startActivity(intent)
    }

    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationRepeat(p0: Animator?) {

    }
    //unregistering the broadcast receiver
    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCounterReceiver)
        super.onDestroy()
    }

}