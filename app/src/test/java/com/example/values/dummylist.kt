package com.example.values
import com.example.falling_words.model.WordsItem


class dummylist{

    fun getDummyWordsList():ArrayList<WordsItem>{
        val wordsList: ArrayList<WordsItem> = ArrayList()

        var word: WordsItem = WordsItem("apple", "saib")
        wordsList?.add(word)
        word = WordsItem("mango", "aaam")
        wordsList?.add(word)
        word = WordsItem("banana", "kela")
        wordsList?.add(word)
        word = WordsItem("grapes", "angoor")
        wordsList?.add(word)
        word = WordsItem("dates", "khajoor")
        wordsList?.add(word)
        word = WordsItem("monday", "sumwaar")
        wordsList?.add(word)
        word = WordsItem("wednesday", "budh")
        wordsList?.add(word)
        word = WordsItem("friday", "jumma")
        wordsList?.add(word)
        word = WordsItem("saturday", "hafta")
        wordsList?.add(word)
        word = WordsItem("sunday", "itwar")
        wordsList?.add(word)

        return wordsList!!
    }

}