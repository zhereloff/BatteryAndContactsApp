package com.example.batteryandcontactsapp.batteryManager

import android.graphics.Color
import com.example.batteryandcontactsapp.services.LoggerService.Companion.log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChartBatteryChargeHistory(private val chart: LineChart) {


    private val entries = ArrayList<Entry>()
    private val timeLabels = ArrayList<String>()
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var xIndex = 0f
    private var dataSet: LineDataSet = LineDataSet(entries, "%")

    init {
        setupChart()
    }

    private fun setupChart() {

        with(chart) {
            setDrawGridBackground(false)
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
        }

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in timeLabels.indices) timeLabels[index] else ""
            }
        }
        chart.axisRight.isEnabled = false
    }

    fun updateChart(batteryLevel: Float) {

        val calendar = Calendar.getInstance()
        val timeString = timeFormat.format(calendar.time)
        timeLabels.add(timeString)

        entries.add(Entry(xIndex++, batteryLevel))
        dataSet = LineDataSet(entries, "%")
        with(dataSet) {
            color = Color.rgb(200, 139, 150)
            valueTextColor = Color.BLACK
            setDrawCircles(true)
            circleRadius = 2f
            circleHoleColor = Color.rgb(19, 103, 162)
            setCircleColor(Color.rgb(19, 103, 162))
            setDrawValues(false)
        }

        chart.data = LineData(dataSet)
        chart.invalidate()

        log(chart.context, "battery chart update with $batteryLevel")
    }

    fun clearChart() {
        entries.clear()
        timeLabels.clear()
        chart.clear()
    }
}