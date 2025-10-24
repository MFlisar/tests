package com.michaelflisar.tests.vico.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.tests.vico.classes.Point
import com.michaelflisar.tests.vico.classes.Range
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.Scroll
import com.patrykandpatrick.vico.multiplatform.cartesian.Zoom
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.ProvideVicoTheme
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.patrykandpatrick.vico.multiplatform.m3.common.rememberM3VicoTheme

object LineChart {

    @Composable
    fun color1() = MaterialTheme.colorScheme.primary

    @Composable
    fun color2() = MaterialTheme.colorScheme.secondary
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    values: List<Point<Double>>,
    range: Range<Double>?,
    xAxisLabel: (value: Double) -> String,
    xAxisTitle: String? = null,
    yAxisTitle: String? = null,
    initialScrollToEnd: Boolean = true,
    consumeMoveEvents: Boolean = false,
) {
    LineChartMulti(
        modifier = modifier,
        values = listOf(values),
        range = range,
        xAxisLabel = xAxisLabel,
        xAxisTitle = xAxisTitle,
        yAxisTitle = yAxisTitle,
        initialScrollToEnd = initialScrollToEnd,
        consumeMoveEvents = consumeMoveEvents
    )
}

@Composable
fun LineChartMulti(
    modifier: Modifier = Modifier,
    values: List<List<Point<Double>>>,
    range: Range<Double>?,
    xAxisLabel: (value: Double) -> String,
    xAxisTitle: String? = null,
    yAxisTitle: String? = null,
    initialScrollToEnd: Boolean = true,
    consumeMoveEvents: Boolean = false,
) {
    val xAxisLabel by rememberUpdatedState(xAxisLabel)

    // Chart Model
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(values.toList().flatten()) {
        modelProducer.runTransaction {
            val filteredValues = values.filter { it.isNotEmpty() }
            if (filteredValues.isNotEmpty()) {
                lineSeries {
                    filteredValues.forEach {
                        series(
                            x = it.map { it.x },
                            y = it.map { it.y }
                        )
                    }
                }
            }
        }
    }

    // Setups
    val titleTextComponent = rememberTextComponent()
    val scrollState = rememberVicoScrollState(
        initialScroll = if (initialScrollToEnd) Scroll.Absolute.End else Scroll.Absolute.Start
    )
    val zoomState = rememberVicoZoomState(
        zoomEnabled = true
    )

    // trying to zoom out to the full range
    LaunchedEffect(range) {
        if (range != null) {
            println("zoom x range: $range | span: ${range.span}")
            zoomState.zoom(Zoom.max(Zoom.x(range.span), Zoom.Content))
        }
    }

    val lineSetup = createLineSetup(LineChart.color1())
    val lineSetup2 = createLineSetup(LineChart.color2())

    // Chart
    ProvideVicoTheme(rememberM3VicoTheme()) {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        lineSetup, lineSetup2
                    ),
                    rangeProvider = remember(range) {
                        if (range != null)
                            CartesianLayerRangeProvider.fixed(range.lower, range.upper)
                        else
                            CartesianLayerRangeProvider.auto()
                    },
                ),
                startAxis = VerticalAxis.rememberStart(
                    titleComponent = if (yAxisTitle != null) {
                        titleTextComponent
                    } else null,
                    title = yAxisTitle
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    //itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                    valueFormatter = remember(xAxisLabel) {
                        object : CartesianValueFormatter {
                            override fun format(
                                context: CartesianMeasuringContext,
                                value: Double,
                                verticalAxisPosition: Axis.Position.Vertical?,
                            ): CharSequence {
                                return xAxisLabel(value)
                            }
                        }
                    },
                    titleComponent = if (xAxisTitle != null) {
                        titleTextComponent
                    } else null,
                    title = xAxisTitle
                )
            ),
            scrollState = scrollState,
            zoomState = zoomState,
            modelProducer = modelProducer,
            modifier = modifier,
            consumeMoveEvents = consumeMoveEvents
        )
    }
}

@Composable
private fun createLineSetup(color: Color): LineCartesianLayer.Line {
    return LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(fill = Fill(color)),
        areaFill = LineCartesianLayer.AreaFill.single(
            fill = Fill(color.copy(alpha = .3f))
        ),
        pointProvider =
            LineCartesianLayer.PointProvider.single(
                LineCartesianLayer.Point(
                    rememberShapeComponent(
                        Fill(color),
                        CorneredShape.Pill
                    )
                )
            ),
    )
}