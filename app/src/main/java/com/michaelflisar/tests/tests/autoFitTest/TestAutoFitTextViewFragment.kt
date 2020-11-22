package com.michaelflisar.tests.tests.autoFitTest

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.michaelflisar.tests.databinding.TestAutoFitTextViewActivityBinding

class TestAutoFitTextViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = TestAutoFitTextViewActivityBinding.inflate(inflater)
        binding.tv1.setBackgroundColor(Color.RED)
        binding.tv2.setBackgroundColor(Color.GREEN)
        binding.tv1.text = "H"
        binding.tv2.text = "H"
        return binding.root
    }
}
