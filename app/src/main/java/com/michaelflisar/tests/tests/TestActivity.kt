package com.michaelflisar.tests.tests

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class TestActivity : AppCompatActivity() {

    companion object {
        val CONTENT_VIEW_ID = 1
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayHomeAsUp = intent.getBooleanExtra(KEY_DISPLAY_HOME_AS_UP, true)
        if (displayHomeAsUp) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        val frame = FrameLayout(this).apply {
            id = CONTENT_VIEW_ID
        }
        setContentView(frame, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        if (savedInstanceState == null) {
            val fragmentClass = intent.getStringExtra(KEY_FRAGMENT_CLASS)
            val f = Class.forName(fragmentClass).newInstance() as Fragment
            supportFragmentManager
                    .beginTransaction()
                    .add(CONTENT_VIEW_ID, f)
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