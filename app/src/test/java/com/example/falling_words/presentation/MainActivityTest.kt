package com.example.falling_words.presentation

import com.example.values.dummylist
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.mockito.MockitoAnnotations.initMocks


class MainActivityTest {

    @InjectMocks
    lateinit var activity: MainActivity
    private var mocks = dummylist()
    @Before
    fun setUp() {
        //initMocks(this)
        //activity = MainActivity()
        MockitoAnnotations.initMocks(this)
    }
    @Test
    fun initializeWords() {
        val words = mocks.getDummyWordsList()
        activity.initializeWords(words)
        assertNotNull(activity.wordsList)
        assertEquals(activity.wordsList?.size, words.size)
    }

    @Test
    fun getNextWord() {
        val words = mocks.getDummyWordsList()
        activity.initializeWords(words)
        val nextWord = activity.getNextWord()
        assertNotNull(nextWord)
        assertEquals(activity.translationIndices.size, 0)
    }

    @Test
    fun getNextTranslation()
    {  val words = mocks.getDummyWordsList()
        activity.initializeWords(words)
        val nextWord = activity.getNextTranslation()
        assertNotNull(nextWord)
        assertEquals(activity.translationIndices.size, 1)
    }
    @Test
    fun isCorrectVisible()
    {
        val words = mocks.getDummyWordsList()
        activity.initializeWords(words)
        val nextWord = activity.getNextWord()
        activity.currentWord = nextWord
        activity.currentTranslationOption = nextWord.text_spa
        assertTrue(activity.isCorrectVisible())
    }
    @Test
    fun isCorrectVisibleNegativeScenario() {
        val words = mocks.getDummyWordsList()
        activity.initializeWords(words)
        val nextWord = activity.getNextWord()
        activity.currentWord = nextWord
        activity.currentTranslationOption = "Wrong Translation abxdf"
        assertFalse(activity.isCorrectVisible())
    }
}