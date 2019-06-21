package com.michaelflisar.tests.tests.DrawerTest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.michaelflisar.tests.R
import com.michaelflisar.tests.base.BaseTestActivity
import com.michaelflisar.tests.databinding.TestDrawerActivityBinding
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder

class TestDrawerActivity : BaseTestActivity(false), View.OnClickListener {

    lateinit var binding: TestDrawerActivityBinding
    lateinit var drawer: Drawer

    private var level = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.test_drawer_activity)
        setSupportActionBar(binding.toolbar)
        initViews()
        updateViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen) {
            drawer.closeDrawer()
            return
        }
        if (goLevelDown()) {
            return
        }
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        goLevelUp()
    }

    // ------------------
    // Functions
    // ------------------

    fun initViews() {
        drawer = DrawerBuilder()
                .withToolbar(binding.toolbar)
                .withActivity(this)
                .build()
    }

    @SuppressLint("SetTextI18n")
    fun updateViews() {
        binding.tvLevel.text = "Current state: level = $level"
        if (level == 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
            drawer.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            drawer.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun goLevelUp(): Boolean {
        if (level == 0) {
            level++
            updateViews()
            return true
        }
        return false
    }

    private fun goLevelDown(): Boolean {
        if (level == 1) {
            level--
            updateViews()
            return true
        }
        return false
    }
}
