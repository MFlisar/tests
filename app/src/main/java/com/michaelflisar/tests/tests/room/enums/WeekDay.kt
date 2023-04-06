package com.michaelflisar.tests.tests.room.enums

import java.text.SimpleDateFormat
import java.util.*

enum class WeekDay(
    val calender: Int
) {
    Monday(Calendar.MONDAY),
    Tuesday(Calendar.TUESDAY),
    Wednesday(Calendar.WEDNESDAY),
    Thursday(Calendar.THURSDAY),
    Friday(Calendar.FRIDAY),
    Saturday(Calendar.SATURDAY),
    Sunday(Calendar.SUNDAY)
    ;

    val index = ordinal

    companion object {
        // könnte später mal von User Setting abhängen!
        fun sorted() = values().toList()

        var SDF_SHORT: SimpleDateFormat = SimpleDateFormat("EEE", Locale.getDefault()) // TUE
        var SDF_LONG: SimpleDateFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // TUESDAY

        private val DATES = values().map { weekDay ->
            Calendar.getInstance().also {
                it.timeInMillis = 0L
                it.set(Calendar.DAY_OF_WEEK, weekDay.calender)
            }.time
        }
    }

    fun getName(short: Boolean) = (if (short) SDF_SHORT else SDF_LONG).format(DATES[ordinal])
        // kein Abkürzungsendung!
        .replace(".", "")
}