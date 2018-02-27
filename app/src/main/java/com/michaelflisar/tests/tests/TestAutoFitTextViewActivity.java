package com.michaelflisar.tests.tests;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;

import com.michaelflisar.tests.R;
import com.michaelflisar.tests.base.BaseTestActivity;
import com.michaelflisar.tests.databinding.TestAutoFitTextViewActivityBinding;

public class TestAutoFitTextViewActivity extends BaseTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestAutoFitTextViewActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.test_auto_fit_text_view_activity);

        binding.tv1.setBackgroundColor(Color.RED);
        binding.tv2.setBackgroundColor(Color.GREEN);

        binding.tv1.setText("H");
        binding.tv2.setText("H");
    }
}
