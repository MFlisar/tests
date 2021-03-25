package com.michaelflisar.tests

import android.content.Context
import com.michaelflisar.tests.tests.TestActivity
import com.michaelflisar.tests.tests.android.TestAutoFitTextViewFragment
import com.michaelflisar.tests.tests.coil.display.ImageDisplayFragment
import com.michaelflisar.tests.tests.fastAdapter.TestExpandableItemFragment
import com.michaelflisar.tests.tests.coil.tinting.ImageTintTestFragment
import com.michaelflisar.tests.tests.rx.RxMapTest

object Definitions {

    val TEST_CASES = listOf(
            // functions
            TestCase.Function("RxMapTest") { RxMapTest.testDelete() },
            // fragments
            TestCase.Fragment("AutoFit TextView Test", TestAutoFitTextViewFragment::class.java),
            TestCase.Fragment("FastAdapter Expandable Test", TestExpandableItemFragment::class.java),
            TestCase.Fragment("Image Tint Test", ImageTintTestFragment::class.java),
            TestCase.Fragment("Image Display Test", ImageDisplayFragment::class.java),
    ).apply {
        sortedBy { it.sortingId }
    }

    sealed class TestCase(val sortingId: Int) {
        abstract val name: String
        abstract fun runTest(context: Context)

        class Function(override val name: String, val function: () -> Unit) : TestCase(0) {
            override fun runTest(context: Context) {
                function()
            }
        }

        class Fragment(override val name: String, val fragment: Class<*>) : TestCase(1) {
            override fun runTest(context: Context) {
                TestActivity.start(context, fragment)
            }
        }
    }
}