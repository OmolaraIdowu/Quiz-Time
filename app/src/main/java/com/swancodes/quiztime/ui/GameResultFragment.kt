package com.swancodes.quiztime.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.swancodes.quiztime.R
import com.swancodes.quiztime.databinding.FragmentGameResultBinding
import com.swancodes.quiztime.util.MAX_NO_OF_QUESTIONS
import com.swancodes.quiztime.util.SCORE_INCREASE
import com.swancodes.quiztime.viewmodel.GameViewModel

class GameResultFragment : Fragment() {
    private lateinit var binding: FragmentGameResultBinding
    private val viewModel: GameViewModel by viewModels()

    private val args: GameResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val score = args.score
        val totalMark = score * SCORE_INCREASE

        val scoreText = getString(R.string.result, totalMark)

        binding.scoreText.text = scoreText

        val totalQuestions = MAX_NO_OF_QUESTIONS
        if (score == totalQuestions) {
            binding.congratulatoryText.text = getString(R.string.game_won_message)
            binding.resultImageView.setImageResource(R.drawable.gold_cup)
        } else {
            binding.congratulatoryText.text = getString(R.string.game_over_message)
            binding.resultImageView.setImageResource(R.drawable.silver_cup)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(GameResultFragmentDirections.toWelcomeFragment())
        }
        binding.newGameButton.setOnClickListener {
            findNavController().navigate(GameResultFragmentDirections.toGameFragment())
        }
        binding.shareButton.setOnClickListener {
            shareScore()
        }
    }

    private fun shareScore() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareMessage = getString(R.string.share_score_message, viewModel.score.value)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_score_title)))
    }
}