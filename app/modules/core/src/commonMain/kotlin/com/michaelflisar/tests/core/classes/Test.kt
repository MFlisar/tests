package com.michaelflisar.tests.core.classes

import androidx.compose.runtime.Composable
import com.michaelflisar.parcelize.Parcelable

interface Test : Parcelable {

    val name: String

    @Composable
    fun Content()
}