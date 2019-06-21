package com.michaelflisar.tests.base

import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity

abstract class BaseTestActivity(val displayHomeAsUp: Boolean = true) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (displayHomeAsUp) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
