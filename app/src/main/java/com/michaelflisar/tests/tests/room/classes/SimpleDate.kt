package com.michaelflisar.tests.tests.room.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/*
 month and day are 0-based
 */
@Parcelize
data class SimpleDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Parcelable {

    companion object {

        fun today(): SimpleDate {
            val now = Calendar.getInstance()
            return from(now)
        }

        fun from(cal: Calendar): SimpleDate {
            return SimpleDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        fun from(timeInMillis: Long): SimpleDate {
            val cal = Calendar.getInstance().apply {
                setTimeInMillis(timeInMillis)
            }
            return from(cal)
        }

        fun parseString(data: String): SimpleDate {
            val parts = data.split(" ")
            return SimpleDate(
                parts[0].toInt(),
                parts[1].toInt(),
                parts[2].toInt()
            )
        }
    }

    val calendarMonth: Int
        get() = month + 1

    // dieser String ist korrekt sortierbar!
    override fun toString() =
        "%04d %02d %02d".format(year, month, day)

    operator fun compareTo(other: SimpleDate): Int {
        if (this.year > other.year) return 1
        if (this.year < other.year) return -1
        if (this.month > other.month) return 1
        if (this.month < other.month) return -1
        if (this.day > other.day) return 1
        if (this.day < other.day) return -1
        return 0
    }
}