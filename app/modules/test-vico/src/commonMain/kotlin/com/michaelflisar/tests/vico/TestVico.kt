package com.michaelflisar.tests.vico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.parcelize.Parcelize
import com.michaelflisar.tests.core.RootScrollableColumn
import com.michaelflisar.tests.core.classes.Test
import com.michaelflisar.tests.vico.charts.LineChart
import com.michaelflisar.tests.vico.classes.Point
import com.michaelflisar.tests.vico.classes.Range
import com.michaelflisar.tests.vico.components.DemoListRegion
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

            LineChart(
                modifier = Modifier.height(200.dp),
                xAxisLabel = {
                    val date = LocalDate.fromEpochDays(it.toInt())
                    date.toString()
                },
                values = values,
                range = dateRange
            )

            DemoListRegion("Content 2", 300.dp)

            LineChart(
                modifier = Modifier.height(400.dp),
                xAxisLabel = {
                    val date = LocalDate.fromEpochDays(it.toInt())
                    date.toString()
                },
                values = values,
                range = dateRange
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

private const val dataCount = 300
private val firstDate = LocalDate(2025, 1, 1)

private val values = List(dataCount) { it ->
    val date = firstDate.plus(it, DateTimeUnit.DAY)
    Point(date.toEpochDays().toDouble(), date.day.toDouble())
}

private val dateRange = Range.create(
    lower = values.first().x,
    upper = values.last().x
)