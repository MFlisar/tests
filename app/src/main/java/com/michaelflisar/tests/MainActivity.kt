package com.michaelflisar.tests

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.michaelflisar.tests.databinding.MainActivityBinding
import com.michaelflisar.tests.tests.RxMapTest
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal val FUNCTIONS: ArrayList<Pair<String, Runnable>> = object : ArrayList<Pair<String, Runnable>>() {
        init {
            add(Pair("RxMapTest", Runnable { RxMapTest.testDelete() }))
        }
    }

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)

        val testCases = createSubActivityButtons() + FUNCTIONS.size
        createFunctionTestButtons()

        binding.tvInfo.text = String.format("Existing MCVE Tests: %d", testCases)
    }

    private fun createSubActivityButtons(): Int {

        var activities = 0
        try {
            val list = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
            activities = list.size - 1

            addHeader("Activities $activities")

            for (info in list) {
                if (info.name != MainActivity::class.java.name) {
                    addItem(info.loadLabel(packageManager).toString(), info)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return activities
    }

    private fun createFunctionTestButtons() {
        addHeader("Functions (${FUNCTIONS.size})")
        for (i in 0..FUNCTIONS.size - 1) {
            addItem(FUNCTIONS[i].first, i)
        }
    }

    private fun addHeader(label: String) {
        val tv = layoutInflater.inflate(R.layout.main_activity_list_header_item, binding.llTests, false) as TextView
        tv.setText(label)
        binding.llTests.addView(tv)
    }

    private fun addItem(label: String, tag: Any) {
        val tv = layoutInflater.inflate(R.layout.main_activity_list_item, binding.llTests, false) as TextView
        tv.tag = tag
        tv.setText(label)
        tv.setOnClickListener(this)
        binding.llTests.addView(tv)
    }


    override fun onClick(v: View) {
        // no security checks, we know what we get
        if (v.tag is ActivityInfo) {
            val info = v.tag as ActivityInfo
            val name = ComponentName(info.packageName, info.name)
            val intent = Intent(Intent.ACTION_MAIN)
            intent.component = name
            startActivity(intent)
        } else {
            val index = v.tag as Int
            FUNCTIONS[index].second.run()

            Toast.makeText(this, "Function executed: ${FUNCTIONS[index].first}", Toast.LENGTH_SHORT).show()
        }


    }
}