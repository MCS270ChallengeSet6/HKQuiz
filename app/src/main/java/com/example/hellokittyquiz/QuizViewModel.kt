package com.example.hellokittyquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "com.example.hellokittyquiz.CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "com.example.hellokittyquiz.IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_1, true, false),
        Question(R.string.question_2, false, false),
        Question(R.string.question_3, true, false),
        Question(R.string.question_4, true, false),
        Question(R.string.question_5, false, false),
        Question(R.string.question_6, false, false)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val questionBankSize: Int
        get() = questionBank.size

    val isAnswered: Boolean
        get() = questionBank[currentIndex].answered

    fun currentQuestionAnswered() {
        questionBank[currentIndex].answered = true
    }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = if (currentIndex == 0){
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }


}