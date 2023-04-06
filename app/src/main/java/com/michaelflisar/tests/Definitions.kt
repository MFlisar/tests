package com.michaelflisar.tests

import androidx.appcompat.app.AppCompatActivity
import com.michaelflisar.tests.tests.TestActivity
import com.michaelflisar.tests.tests.android.TestAutoFitTextViewFragment
import com.michaelflisar.tests.tests.dialog.TestDialogFragment
import com.michaelflisar.tests.tests.coil.display.ImageDisplayFragment
import com.michaelflisar.tests.tests.coil.tinting.ImageTintTestFragment
import com.michaelflisar.tests.tests.fastAdapter.TestExpandableItemFragment
import com.michaelflisar.tests.tests.flowUIProblem.FlowUIFragment
import com.michaelflisar.tests.tests.rx.RxMapTest

object Definitions {

    val TEST_CASES = listOf(
        // functions
        TestCase.Function("RxMapTest") { RxMapTest.testDelete() },
        // functions that open a fragment
        TestCase.Function("Dialog") {
            TestDialogFragment
                .newInstance()
                .show(it.supportFragmentManager, "dialog")
        },
        // content fragments
        TestCase.Fragment("AutoFit TextView Test", TestAutoFitTextViewFragment::class.java),
        TestCase.Fragment("FastAdapter Expandable Test", TestExpandableItemFragment::class.java),
        TestCase.Fragment("Image Tint Test", ImageTintTestFragment::class.java),
        TestCase.Fragment("Image Display Test", ImageDisplayFragment::class.java),
        TestCase.Fragment("Flow UI Test", FlowUIFragment::class.java)
    ).sortedBy {
        it.sortingId
    }

    sealed class TestCase(val sortingId: Int) {
        abstract val name: String
        abstract fun runTest(activity: AppCompatActivity)

        class Function(
            override val name: String,
            val function: (activity: AppCompatActivity) -> Unit
        ) : TestCase(0) {
            override fun runTest(activity: AppCompatActivity) {
                function(activity)
            }
        }

        class Fragment(override val name: String, val fragment: Class<*>) : TestCase(1) {
            override fun runTest(activity: AppCompatActivity) {
                TestActivity.start(activity, fragment)
            }
        }
    }
}