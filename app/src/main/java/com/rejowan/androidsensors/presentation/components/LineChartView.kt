package com.rejowan.androidsensors.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.rejowan.chart.charts.LineChart
import com.rejowan.chart.components.XAxis
import com.rejowan.chart.data.Entry
import com.rejowan.chart.data.LineData
import com.rejowan.chart.data.LineDataSet

@Composable
fun LineChartView(
    data: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
    showFill: Boolean = true
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setPinchZoom(false)
                setDrawGridBackground(false)
                isDragEnabled = false
                setScaleEnabled(false)
                setTouchEnabled(false)
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                setViewPortOffsets(4f, 4f, 4f, 4f)
                this.data = LineData()
            }
        },
        update = { chart ->
            val lineData = chart.data ?: return@AndroidView

            var set = lineData.getDataSetByIndex(0) as? LineDataSet
            if (set == null) {
                set = LineDataSet(mutableListOf(), "").apply {
                    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    cubicIntensity = 0.2f
                    setDrawFilled(showFill)
                    setDrawCircles(false)
                    setDrawValues(false)
                    lineWidth = 2f
                    color = lineColor.toArgb()
                    this.fillColor = fillColor.toArgb()
                    fillAlpha = (fillColor.alpha * 255).toInt()
                }
                lineData.addDataSet(set)
            }

            set.clear()
            data.forEachIndexed { index, value ->
                set.addEntry(Entry(index.toFloat(), value))
            }

            lineData.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}

@Composable
fun MultiLineChartView(
    xData: List<Float>,
    yData: List<Float>,
    zData: List<Float>,
    modifier: Modifier = Modifier,
    xColor: Color = MaterialTheme.colorScheme.primary,
    yColor: Color = MaterialTheme.colorScheme.error,
    zColor: Color = MaterialTheme.colorScheme.tertiary,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setPinchZoom(true)
                setDrawGridBackground(false)
                isDragEnabled = true
                setScaleEnabled(true)
                setTouchEnabled(true)
                legend.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    setDrawAxisLine(false)
                    this.textColor = textColor.toArgb()
                }

                axisRight.isEnabled = false
                axisLeft.apply {
                    setDrawGridLines(false)
                    this.textColor = textColor.toArgb()
                }

                setViewPortOffsets(40f, 16f, 16f, 40f)
                this.data = LineData()
            }
        },
        update = { chart ->
            val lineData = chart.data ?: return@AndroidView

            var setX = lineData.getDataSetByIndex(0) as? LineDataSet
            var setY = lineData.getDataSetByIndex(1) as? LineDataSet
            var setZ = lineData.getDataSetByIndex(2) as? LineDataSet

            if (setX == null) {
                setX = createLineDataSet(xColor, "X")
                lineData.addDataSet(setX)
            }
            if (setY == null) {
                setY = createLineDataSet(yColor, "Y")
                lineData.addDataSet(setY)
            }
            if (setZ == null) {
                setZ = createLineDataSet(zColor, "Z")
                lineData.addDataSet(setZ)
            }

            setX.clear()
            setY.clear()
            setZ.clear()

            xData.forEachIndexed { index, value ->
                setX.addEntry(Entry(index.toFloat(), value))
            }
            yData.forEachIndexed { index, value ->
                setY.addEntry(Entry(index.toFloat(), value))
            }
            zData.forEachIndexed { index, value ->
                setZ.addEntry(Entry(index.toFloat(), value))
            }

            lineData.notifyDataChanged()
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    )
}

private fun createLineDataSet(color: Color, label: String): LineDataSet {
    return LineDataSet(mutableListOf(), label).apply {
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.2f
        setDrawFilled(false)
        setDrawCircles(false)
        setDrawValues(false)
        lineWidth = 2f
        this.color = color.toArgb()
    }
}
