package com.michaelflisar.tests.tests.autoFitTest

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.michaelflisar.tests.R
import com.michaelflisar.tests.base.BaseTestActivity
import com.michaelflisar.tests.databinding.TestAutoFitTextViewActivityBinding

class TestAutoFitTextViewActivity : BaseTestActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<TestAutoFitTextViewActivityBinding>(this, R.layout.test_auto_fit_text_view_activity)

        binding.tv1.setBackgroundColor(Color.RED)
        binding.tv2.setBackgroundColor(Color.GREEN)

        binding.tv1.text = "H"
        binding.tv2.text = "H"
    }
}
