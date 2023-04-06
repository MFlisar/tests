package com.michaelflisar.tests.tests.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michaelflisar.tests.databinding.FragmentAutoFitTextViewTestBinding
import kotlin.random.Random

class TestAutoFitTextViewFragment : Fragment() {

    var text: String = ""
    private val MAX_LENGTH = 20

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAutoFitTextViewTestBinding.inflate(inflater)
        binding.btChangeText.setOnClickListener {
            updateTexts(binding)
        }
        updateTexts(binding)
        return binding.root
    }

    private fun updateTexts(binding: FragmentAutoFitTextViewTestBinding) {
        generateNextString()
        listOf(
            binding.tv1,
            binding.tv2,
            binding.tv3,
            binding.tv4
        ).forEach {
            it.text = text
        }
        binding.tvText.text = "Full Text = $text"
        binding.tvInfo.text = "Length = ${text.length}"
    }

    // Step through random string from length 1 to [max], then restart from 1...
    private fun generateNextString(max: Int = MAX_LENGTH) {
        if (text.length >= max) {
            text = ""
        }
        val c = (Random.nextInt(26) + 'A'.code).toChar()
        text += c.toString()
    }
}
