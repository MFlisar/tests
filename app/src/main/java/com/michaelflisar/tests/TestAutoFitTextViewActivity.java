package com.michaelflisar.tests;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.michaelflisar.tests.databinding.TestAutoFitTextViewActivityBinding;

public class TestAutoFitTextViewActivity extends AppCompatActivity {

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
