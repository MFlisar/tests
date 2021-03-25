package com.michaelflisar.tests

import android.app.Application
import com.michaelflisar.lumberjack.L
import com.michaelflisar.tests.tests.coil.ImageManager
import timber.log.ConsoleTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        L.plant(ConsoleTree())
        ImageManager.init(this)
    }
}