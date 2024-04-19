package com.rejowan.androidsensors

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rejowan.androidsensors.databinding.ActivitySensorsBinding

class SensorsActivity : AppCompatActivity() {

    private val binding: ActivitySensorsBinding by lazy {
        ActivitySensorsBinding.inflate(layoutInflater)
    }

    private var sensorName = ""
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorName = intent.getStringExtra("sensor").toString()
        binding.title.text = sensorName

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if (sensorName == "accelerometer") {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }

        sensor?.let { setupSensorDetails(it) }

    }

    private fun setupSensorDetails(sensor: Sensor) {

        binding.type.text = sensor.stringType
        binding.manufacturer.text = sensor.vendor
        binding.power.text = sensor.power.toString()
        binding.maxRange.text = sensor.maximumRange.toString()
        binding.resolution.text = sensor.resolution.toString()
        binding.wakeUp.text = if (sensor.isWakeUpSensor) "Yes" else "No"


        binding.lineChart.description.isEnabled = false
        binding.lineChart.setPinchZoom(true)
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(true)
        binding.lineChart.setTouchEnabled(true)


        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.textColor = getColor(R.color.textColor)
        xAxis.setDrawAxisLine(false)


        binding.lineChart.axisRight.isEnabled = false

        val yAxis = binding.lineChart.axisLeft
        yAxis.textColor = getColor(R.color.textColor)

        binding.lineChart.axisLeft.setDrawGridLines(false)

        binding.lineChart.animateXY(1500, 1500)


        val data = LineData()
        binding.lineChart.data = data


        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    binding.xAxis.text = "$x"
                    binding.xAxis.setTextColor(getColor(R.color.colorPrimary))
                    binding.yAxis.text = "$y"
                    binding.yAxis.setTextColor(getColor(R.color.red))
                    binding.zAxis.text = "$z"
                    binding.zAxis.setTextColor(getColor(R.color.green))

                    val lineData = binding.lineChart.data

                    if (lineData != null) {

                        var set: ILineDataSet? = lineData.getDataSetByIndex(0)
                        var set2: ILineDataSet? = lineData.getDataSetByIndex(1)
                        var set3: ILineDataSet? = lineData.getDataSetByIndex(2)

                        if (set == null || set2 == null || set3 == null) {
                            set = createSet(getColor(R.color.colorPrimary))
                            lineData.addDataSet(set)

                            set2 = createSet(getColor(R.color.red))
                            lineData.addDataSet(set2)

                            set3 = createSet(getColor(R.color.green))
                            lineData.addDataSet(set3)
                        }


                        lineData.addEntry(Entry(set.entryCount.toFloat(), x), 0)
                        lineData.addEntry(Entry(set2.entryCount.toFloat(), y), 1)
                        lineData.addEntry(Entry(set3.entryCount.toFloat(), z), 2)

                        if (set.entryCount > 25) {
                            set.removeFirst()
                            for (i in 0 until set.entryCount) {
                                val entry = set.getEntryForIndex(i)
                                entry.x = entry.x - 1
                            }
                        }

                        if (set2.entryCount > 25) {
                            set2.removeFirst()
                            for (i in 0 until set2.entryCount) {
                                val entry = set2.getEntryForIndex(i)
                                entry.x = entry.x - 1
                            }
                        }

                        if (set3.entryCount > 25) {
                            set3.removeFirst()
                            for (i in 0 until set3.entryCount) {
                                val entry = set3.getEntryForIndex(i)
                                entry.x = entry.x - 1
                            }
                        }

                        lineData.notifyDataChanged()
                        binding.lineChart.notifyDataSetChanged()
                        binding.lineChart.invalidate()
                    }

                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        sensorManager.registerListener(
            sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
        )


    }

    private fun createSet(color: Int): ILineDataSet {
        val lineDataSet = LineDataSet(null, "")
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.4f
        lineDataSet.setDrawFilled(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 1.8f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(getColor(R.color.textColor))
        lineDataSet.highLightColor = Color.rgb(244, 117, 117)
        lineDataSet.color = color
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        return lineDataSet
    }


}