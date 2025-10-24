package com.michaelflisar.tests.core.tests

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.parcelize.Parcelize
import com.michaelflisar.tests.core.classes.Test

@Parcelize
object TestTest : Test {

    override val name = "Test"

    @Composable
    override fun Content() {
        Text("A simple empty test page")
    }
}