package com.michaelflisar.tests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.michaelflisar.tests.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        addHeader("Functions (${Definitions.TEST_CASES.count { it is Definitions.TestCase.Function }})")
        createFunctionTestButtons()
        addHeader("Fragments (${Definitions.TEST_CASES.count { it is Definitions.TestCase.Fragment }})")
        createFragmentTestButtons()

        binding.tvInfo.text = "Existing MCVE Tests: ${Definitions.TEST_CASES.size}"
    }

    private fun createFunctionTestButtons() {
        Definitions.TEST_CASES.filterIsInstance<Definitions.TestCase.Function>().forEach {
            addItem(it.name, Definitions.TEST_CASES.indexOf(it))
        }
    }

    private fun createFragmentTestButtons() {
        Definitions.TEST_CASES.filterIsInstance<Definitions.TestCase.Fragment>().forEach {
            addItem(it.name, Definitions.TEST_CASES.indexOf(it))
        }
    }

    private fun addHeader(label: String) {
        val tv = layoutInflater.inflate(R.layout.item_test_case_header, binding.llTests, false) as TextView
        tv.text = label
        binding.llTests.addView(tv)
    }

    private fun addItem(label: String, tag: Any) {
        val tv = layoutInflater.inflate(R.layout.item_test_case, binding.llTests, false) as TextView
        tv.tag = tag
        tv.text = label
        tv.setOnClickListener(this)
        binding.llTests.addView(tv)
    }


    override fun onClick(v: View) {
        val index = v.tag as Int
        val testCase = Definitions.TEST_CASES[index]
        testCase.runTest(this)
        if (testCase is Definitions.TestCase.Function)
            Toast.makeText(this, "Function executed: ${testCase.name}", Toast.LENGTH_SHORT).show()
    }
}