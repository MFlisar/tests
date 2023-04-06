package com.michaelflisar.tests.tests.room.classes

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.floor

// unterstützt bis zu 10000 "Subsätze" bzw einen Supersatz aus bis zu 10000 Übungen...
// value: 0     => main = 0 und sub = 0
// value: 1     => main = 0 und sub = 1
// value: 10000 => main = 1 und sub = 1
// value: 10001 => main = 1 und sub = 1
sealed class MyIndex : Comparable<MyIndex>, Parcelable {

    internal abstract val value: Int
    abstract val main: Int
    abstract val sub: Int

    companion object {
        private const val MAX = 10000
    }

    @Parcelize
    data class MySingle(override val main: Int) : MyIndex() {
        companion object {
            fun create(value: Int) : MySingle {
                val main = floor(value.toFloat() / MAX.toFloat()).toInt()
                return MySingle(main)
            }
        }
        @IgnoredOnParcel
        override val value = main * MAX
        @IgnoredOnParcel
        override val sub = 0
        @IgnoredOnParcel
        override val info = "$main (value = $value)"
    }

    @Parcelize
    data class MyDouble(override val main: Int, override val sub: Int) : MyIndex() {
        companion object {
            fun create(value: Int) : MyDouble {
                val main = floor(value.toFloat() / MAX.toFloat()).toInt()
                val sub = value % MAX
                return MyDouble(main, sub)
            }
        }
        @IgnoredOnParcel
        override val value = main * MAX + sub
        @IgnoredOnParcel
        override val info = "$main|$sub (value = $value)"
    }

    abstract val info: String

    override fun compareTo(other: MyIndex): Int {
        return value.compareTo(other.value)
    }
}