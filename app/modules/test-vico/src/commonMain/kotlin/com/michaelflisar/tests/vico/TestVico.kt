package com.michaelflisar.tests.vico

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.parcelize.Parcelize
import com.michaelflisar.tests.core.RootScrollableColumn
import com.michaelflisar.tests.core.classes.Test
import com.michaelflisar.tests.vico.charts.LineChart
import com.michaelflisar.tests.vico.classes.Point
import com.michaelflisar.tests.vico.classes.Range
import com.michaelflisar.tests.vico.components.DemoListRegion
import kotlinx.coroutines.delay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Parcelize
object TestVico : Test {

    override val name = "Vico"

    @Composable
    override fun Content() {
        RootScrollableColumn {

            DemoListRegion("Content 1", 200.dp)

            val values = remember { mutableStateOf<List<Point<Double>>>(emptyList()) }
            val range = remember { mutableStateOf<Range<Double>?>(null) }

            LaunchedEffect(Unit) {
                // Simulate data loading
                delay(1000)
                values.value = TestData.values
                range.value = TestData.dateRange
            }

            LineChart(
                modifier = Modifier.height(200.dp),
                xAxisLabel = {
                    val date = LocalDate.fromEpochDays(it.toInt())
                    date.toString()
                },
                values = values.value,
                range = range.value
            )

            DemoListRegion("Content 2", 300.dp)

            LineChart(
                modifier = Modifier.height(400.dp),
                xAxisLabel = {
                    val date = LocalDate.fromEpochDays(it.toInt())
                    date.toString()
                },
                values = values.value,
                range = range.value
            )

            DemoListRegion("Content 3", 200.dp)
            DemoListRegion("Content 4", 300.dp)
            DemoListRegion("Content 5", 100.dp)

        }
    }
}

// ------------------
// Test Data
// ------------------

object TestData {

    const val COUNT = 300

    val firstDate = LocalDate(2025, 1, 1)
    val values = List(COUNT) { it ->
        val date = firstDate.plus(it, DateTimeUnit.DAY)
        Point(date.toEpochDays().toDouble(), date.day.toDouble())
    }
    val dateRange = Range.create(
        lower = values.first().x,
        upper = values.last().x
    )
}

