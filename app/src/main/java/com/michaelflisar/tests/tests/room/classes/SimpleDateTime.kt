package com.michaelflisar.tests.tests.room.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class SimpleDateTime(
    val timeInMillis: Long,
    val date: SimpleDate,
    val time: SimpleTime
) : Parcelable {
    companion object {
        fun now(): SimpleDateTime {
            val now = Calendar.getInstance()
            return from(now)
        }

        fun from(cal: Calendar): SimpleDateTime {
            return SimpleDateTime(
                cal.timeInMillis,
                SimpleDate.from(cal),
                SimpleTime.from(cal)
            )
        }

        fun from(timeInMillis: Long): SimpleDateTime {
            return SimpleDateTime(
                timeInMillis,
                SimpleDate.from(timeInMillis),
                SimpleTime.from(timeInMillis)
            )
        }
    }
}