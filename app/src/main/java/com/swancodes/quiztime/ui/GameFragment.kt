package com.swancodes.quiztime.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.swancodes.quiztime.R
import com.swancodes.quiztime.databinding.FragmentGameBinding
import com.swancodes.quiztime.util.MAX_NO_OF_QUESTIONS
import com.swancodes.quiztime.viewmodel.GameViewModel

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        // Start the game by showing the first question
        viewModel.startGame()

        binding.nextButton.setOnClickListener {
            val selectedOption = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (selectedOption != -1) {
                val selectedAnswer = when (selectedOption) {
                    R.id.firstAnswerRadioButton -> binding.firstAnswerRadioButton.text.toString()
                    R.id.secondAnswerRadioButton -> binding.secondAnswerRadioButton.text.toString()
                    R.id.thirdAnswerRadioButton -> binding.thirdAnswerRadioButton.text.toString()
                    R.id.fourthAnswerRadioButton -> binding.fourthAnswerRadioButton.text.toString()
                    else -> ""
                }
                // Clear the selected answer for the next question
                binding.questionRadioGroup.clearCheck()

                val isAnswerCorrect = viewModel.isAnswerCorrect(selectedAnswer)

                if (isAnswerCorrect) {
                    viewModel.updateScore()
                }
                if (viewModel.hasMoreQuestions()) {
                    viewModel.nextQuestion()
                } else {
                    val score = viewModel.score.value ?: 0
                    findNavController().navigate(GameFragmentDirections.toGameResultFragment(score))
                }
            }
        }
        binding.quitGameText.setOnClickListener {
            exitGame()
        }
    }

    private fun setUpObservers() {
        viewModel.currentQuestionCount.observe(viewLifecycleOwner) { questionCount ->
            // Update question indicator
            val totalQuestions = MAX_NO_OF_QUESTIONS
            binding.questionIndicatorText.text =
                getString(R.string.question_indicator_text, questionCount, totalQuestions)

            binding.nextButton.text = if (questionCount < totalQuestions) {
                getString(R.string.next)
            } else {
                getString(R.string.submit)
            }
            updateDashes(questionCount)
        }
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            // Show the current question text and options
            binding.questionText.text = question.questionText
            binding.firstAnswerRadioButton.text = question.options[0]
            binding.secondAnswerRadioButton.text = question.options[1]
            binding.thirdAnswerRadioButton.text = question.options[2]
            binding.fourthAnswerRadioButton.text = question.options[3]
        }
    }

    private fun exitGame() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.quit_game))
            .setMessage(getString(R.string.quit_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                findNavController().navigate(GameFragmentDirections.toWelcomeFragment())
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateDashes(questionCount: Int) {
        val dashViews = listOf(binding.dashOne, binding.dashTwo, binding.dashThree)

        for (i in dashViews.indices) {
            // Set the background drawable based on whether the question has been answered
            val backgroundDrawable = if (i < questionCount) {
                R.drawable.green_dash // for answered dashes
            } else {
                R.drawable.gray_dash // for unanswered dashes
            }
            // Set the background drawable for the corresponding dash view
            dashViews[i].setBackgroundResource(backgroundDrawable)
        }
    }
}