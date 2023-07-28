package com.swancodes.quiztime.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.swancodes.quiztime.data.model.Question
import com.swancodes.quiztime.util.MAX_NO_OF_QUESTIONS
import com.swancodes.quiztime.util.allQuestions

class GameViewModel : ViewModel() {

    private var _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private var _currentQuestionCount = MutableLiveData(0)
    val currentQuestionCount: LiveData<Int> get() = _currentQuestionCount

    private var _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> get() = _currentQuestion

    private var currentQuestionIndex = 0

    // List of questions used in the game
    private var questionsList: MutableList<Question> = mutableListOf()

    init {
        questionsList.addAll(allQuestions.shuffled().take(MAX_NO_OF_QUESTIONS))
        _currentQuestionCount.value = 0
    }


    fun startGame() {
        currentQuestionIndex = 0
        _score.value = 0
        _currentQuestionCount.value = 1
        _currentQuestion.value = questionsList[currentQuestionIndex]
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questionsList.size - 1) {
            currentQuestionIndex++
            _currentQuestionCount.value = _currentQuestionCount.value?.plus(1)
            _currentQuestion.value = questionsList[currentQuestionIndex]
        }
    }

    fun isAnswerCorrect(selectedAnswer: String): Boolean {
        val correctAnswer = currentQuestion.value?.correctAnswer ?: ""
        return selectedAnswer == correctAnswer
    }

    fun updateScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun hasMoreQuestions(): Boolean {
        return currentQuestionIndex < questionsList.size - 1
    }
}