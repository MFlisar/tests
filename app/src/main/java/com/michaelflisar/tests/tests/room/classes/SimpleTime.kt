package com.michaelflisar.tests.tests.room.classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class SimpleTime(
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0
) : Parcelable {

    enum class Format(val splitter: String) {
        Database("-"),
        DisplayHM(":"),
        DisplayHMS(":")
    }

    companion object {

        fun now(): SimpleTime {
            val now = Calendar.getInstance()
            return from(now)
        }

        fun from(cal: Calendar): SimpleTime {
            return SimpleTime(
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND)
            )
        }

        fun from(timeInMillis: Long): SimpleTime {
            val cal = Calendar.getInstance().apply {
                setTimeInMillis(timeInMillis)
            }
            return from(cal)
        }

        fun create(cal: Calendar) = SimpleTime(
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )

        fun createFromSeconds(seconds: Int) : SimpleTime {
            val hours = seconds / 60 / 60
            val mins = (seconds - hours * 60 * 60) / 60
            val secs = (seconds - hours * 60 * 60 - mins * 60)
            return SimpleTime(hours, mins, secs)
        }

        fun parseString(data: String, format: Format): SimpleTime {
            val parts = data.split(format.splitter)
            val containsAMPM = data.contains("AM") || data.contains("PM")
            return when (format) {
                Format.Database -> SimpleTime(
                    parts[0].toInt(),
                    parts[1].toInt(),
                    parts[2].toInt()
                )
                Format.DisplayHMS -> {
                    if (containsAMPM) {
                        val subParts = parts[2].split(" ")
                        // 12:00 am == 0:00
                        // 12:00 pm == 12:00
                        val h = parts[0].toInt()
                        val m = parts[1].toInt()
                        val s = subParts[0].toInt()
                        val isAM = subParts[1].uppercase() == "AM"
                        val hour = if (isAM) h else (h + 12)
                        SimpleTime(hour, m, s)
                    } else {
                        SimpleTime(
                            parts[0].toInt(),
                            parts[1].toInt(),
                            parts[2].toInt()
                        )
                    }
                }
                Format.DisplayHM -> {
                    if (containsAMPM) {
                        val subParts = parts[1].split(" ")
                        // 12:00 am == 0:00
                        // 12:00 pm == 12:00
                        val h = parts[0].toInt()
                        val m = subParts[0].toInt()
                        val s = 0
                        val isAM = subParts[1].uppercase() == "AM"
                        val hour = if (isAM) h else (h + 12)
                        SimpleTime(hour, m, s)
                    } else {
                        SimpleTime(
                            parts[0].toInt(),
                            parts[1].toInt(),
                            0
                        )
                    }
                }
            }
        }
    }

    // dieser String ist korrekt sortierbar!
    override fun toString() =
        "%02d %02d %02d".format(hour, minute, second)

    fun toString(format: Format): String {
        return when (format) {
            Format.Database -> String.format("%02d%s%02d%s%02d", hour, format.splitter, minute, format.splitter, second)
            Format.DisplayHMS -> {
                String.format("%02d%s%02d%s%02d", hour, format.splitter, minute, format.splitter, second)
            }
            Format.DisplayHM -> {
                    String.format("%02d%s%02d", hour, format.splitter, minute)
            }
        }
    }

    operator fun compareTo(other: SimpleTime): Int {
        if (this.hour > other.hour) return 1
        if (this.hour < other.hour) return -1
        if (this.minute > other.minute) return 1
        if (this.minute < other.minute) return -1
        if (this.second > other.second) return 1
        if (this.second < other.second) return -1
        return 0
    }
}