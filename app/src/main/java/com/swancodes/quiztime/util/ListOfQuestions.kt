package com.swancodes.quiztime.util

import com.swancodes.quiztime.data.model.Question

const val MAX_NO_OF_QUESTIONS = 3
const val SCORE_INCREASE = 10

// List with all the questions for the Game
val allQuestions: MutableList<Question> = mutableListOf(
    Question(
        questionText = "What programming language is primarily used for Android app development?",
        options = listOf("C++", "Kotlin", "Python", "Swift"),
        correctAnswer = "Kotlin"
    ),
    Question(
        questionText = "Which Kotlin extension function is commonly used for initializing and setting up a RecyclerView in Android?",
        options = listOf("'setAdapter()'", "'initRecyclerView()'", "'setLayoutManager()'", "'findViewById()'"),
        correctAnswer = "'setLayoutManager()'"
    ),
    Question(
        questionText = "How do you declare a nullable variable in Kotlin?",
        options = listOf("'val z: String? = \"Hello\"'", "'var a = 10'", "'val y: String = null'", "'var w = Nullable'"),
        correctAnswer = "'val z: String? = \"Hello\"'"
    )
)