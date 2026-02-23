package com.rejowan.androidsensors.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.rejowan.androidsensors.data.model.SensorCategory
import com.rejowan.androidsensors.data.model.SensorData
import com.rejowan.androidsensors.data.model.SensorInfo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SensorRepository(context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensorTypeToCategory = mapOf(
        // Motion Sensors
        Sensor.TYPE_ACCELEROMETER to SensorCategory.MOTION,
        Sensor.TYPE_GYROSCOPE to SensorCategory.MOTION,
        Sensor.TYPE_GRAVITY to SensorCategory.MOTION,
        Sensor.TYPE_LINEAR_ACCELERATION to SensorCategory.MOTION,
        Sensor.TYPE_STEP_COUNTER to SensorCategory.MOTION,
        Sensor.TYPE_STEP_DETECTOR to SensorCategory.MOTION,
        Sensor.TYPE_SIGNIFICANT_MOTION to SensorCategory.MOTION,

        // Position Sensors
        Sensor.TYPE_MAGNETIC_FIELD to SensorCategory.POSITION,
        Sensor.TYPE_PROXIMITY to SensorCategory.POSITION,
        Sensor.TYPE_ROTATION_VECTOR to SensorCategory.POSITION,
        Sensor.TYPE_GAME_ROTATION_VECTOR to SensorCategory.POSITION,
        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR to SensorCategory.POSITION,

        // Environment Sensors
        Sensor.TYPE_LIGHT to SensorCategory.ENVIRONMENT,
        Sensor.TYPE_PRESSURE to SensorCategory.ENVIRONMENT,
        Sensor.TYPE_AMBIENT_TEMPERATURE to SensorCategory.ENVIRONMENT,
        Sensor.TYPE_RELATIVE_HUMIDITY to SensorCategory.ENVIRONMENT,

        // Other
        Sensor.TYPE_HEART_RATE to SensorCategory.OTHER
    )

    fun getAvailableSensors(): List<SensorInfo> {
        val sensors = mutableListOf<SensorInfo>()

        sensorTypeToCategory.forEach { (type, category) ->
            val sensor = sensorManager.getDefaultSensor(type)
            if (sensor != null) {
                sensors.add(SensorInfo.fromSensor(sensor, category))
            }
        }

        return sensors.sortedBy { it.category.ordinal }
    }

    fun getSensorsByCategory(): Map<SensorCategory, List<SensorInfo>> {
        return getAvailableSensors().groupBy { it.category }
    }

    fun getSensor(sensorType: Int): Sensor? {
        return sensorManager.getDefaultSensor(sensorType)
    }

    fun getSensorInfo(sensorType: Int): SensorInfo? {
        val sensor = getSensor(sensorType) ?: return null
        val category = sensorTypeToCategory[sensorType] ?: SensorCategory.OTHER
        return SensorInfo.fromSensor(sensor, category)
    }

    fun observeSensorData(sensorType: Int, samplingPeriod: Int = SensorManager.SENSOR_DELAY_NORMAL): Flow<SensorData> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        if (sensor == null) {
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                trySend(
                    SensorData(
                        timestamp = event.timestamp,
                        values = event.values.copyOf(),
                        accuracy = event.accuracy
                    )
                )
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, sensor, samplingPeriod)

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
