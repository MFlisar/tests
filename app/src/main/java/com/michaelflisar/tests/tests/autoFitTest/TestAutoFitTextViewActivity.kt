package com.michaelflisar.tests.tests.autoFitTest

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.michaelflisar.tests.base.BaseTestActivity
import com.michaelflisar.tests.databinding.TestAutoFitTextViewActivityBinding

class TestAutoFitTextViewActivity : BaseTestActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = TestAutoFitTextViewActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.tv1.setBackgroundColor(Color.RED)
        binding.tv2.setBackgroundColor(Color.GREEN)

        binding.tv1.text = "H"
        binding.tv2.text = "H"
    }
}
