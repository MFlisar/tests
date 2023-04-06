package com.michaelflisar.tests.tests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.michaelflisar.tests.R
import com.michaelflisar.tests.databinding.ActivityMainBinding
import com.michaelflisar.tests.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity() {

    companion object {
        val KEY_FRAGMENT_CLASS = "fragment"
        val KEY_DISPLAY_HOME_AS_UP = "displayHomeAsUp"
        fun start(context: Context, fragment: Class<*>, displayHomeAsUp: Boolean = true) {
            val intent = Intent(context, TestActivity::class.java).apply {
                putExtra(KEY_FRAGMENT_CLASS, fragment.name)
                putExtra(KEY_DISPLAY_HOME_AS_UP, displayHomeAsUp)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(LayoutInflater.from(this))
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)
        val displayHomeAsUp = intent.getBooleanExtra(KEY_DISPLAY_HOME_AS_UP, true)
        if (displayHomeAsUp) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState == null) {
            val fragmentClass = intent.getStringExtra(KEY_FRAGMENT_CLASS)
            val f = Class.forName(fragmentClass).newInstance() as Fragment
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content, f)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}