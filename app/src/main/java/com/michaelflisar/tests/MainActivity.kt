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
import com.michaelflisar.tests.databinding.MainActivityBinding
import com.michaelflisar.tests.tests.RxMapTest
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal val FUNCTIONS: ArrayList<Pair<String, Runnable>> = object : ArrayList<Pair<String, Runnable>>() {
        init {
            add(Pair("RxMapTest", Runnable { RxMapTest.testDelete() }))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)

        val activities = createSubActivityButtons(binding)
        binding.tvInfo.text = String.format("Existing MCVE Tests: %d", activities)

        val functions = createFunctionTestButtons(binding)
        binding.tvInfo2.setText(String.format("Existing Function Tests: %d", functions))
    }

    private fun createSubActivityButtons(binding: MainActivityBinding): Int {
        var activities = 0
        try {
            val list = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
            activities = list.size - 1
            for (info in list) {
                if (info.name != MainActivity::class.java.name) {
                    val tv = layoutInflater.inflate(R.layout.main_activity_list_item, binding.llActivities, false) as TextView
                    tv.tag = info
                    tv.text = info.loadLabel(packageManager)
                    tv.setOnClickListener(this)
                    binding.llActivities.addView(tv)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return activities
    }

    private fun createFunctionTestButtons(binding: MainActivityBinding): Int {
        for (i in 0..FUNCTIONS.size - 1) {
            val tv = layoutInflater.inflate(R.layout.main_activity_list_item, binding.llActivities, false) as TextView
            tv.tag = i
            tv.setText(FUNCTIONS[i].first)
            tv.setOnClickListener(this)
            binding.llFunctions.addView(tv)
        }
        return FUNCTIONS.size
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
        }


    }
}