package com.michaelflisar.tests.vico.classes

import androidx.compose.runtime.Stable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Stable
class Range<T : Comparable<T>> private constructor(
    val lower: T,
    val upper: T,
    val minus: (T, T) -> T,
) {
    companion object {

        fun create(lower: Int, upper: Int) = Range(lower, upper) { a, b -> a - b }
        fun create(lower: Long, upper: Long) = Range(lower, upper) { a, b -> a - b }
        fun create(lower: Float, upper: Float) = Range(lower, upper) { a, b -> a - b }
        fun create(lower: Double, upper: Double) = Range(lower, upper) { a, b -> a - b }

        fun create(lower: LocalDate, upper: LocalDate) = Range(
            lower,
            upper
        ) { a, b -> LocalDate.fromEpochDays(a.toEpochDays() - b.toEpochDays()) }

        @OptIn(ExperimentalTime::class)
        fun create(lower: LocalDateTime, upper: LocalDateTime) = Range(lower, upper) { a, b ->
            val millisA = a.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val millisB = b.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            Instant.fromEpochMilliseconds(millisA - millisB)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        }

    }

    override fun toString(): String {
        return "Range(lower=$lower, upper=$upper)"
    }

    val span: T
        get() = minus(upper, lower)
}