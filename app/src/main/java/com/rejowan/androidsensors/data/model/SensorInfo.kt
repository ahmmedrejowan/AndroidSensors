package com.rejowan.androidsensors.data.model

import android.hardware.Sensor

data class SensorInfo(
    val type: Int,
    val name: String,
    val stringType: String,
    val vendor: String,
    val version: Int,
    val resolution: Float,
    val maximumRange: Float,
    val power: Float,
    val minDelay: Int,
    val maxDelay: Int,
    val fifoMaxEventCount: Int,
    val fifoReservedEventCount: Int,
    val isWakeUpSensor: Boolean,
    val isDynamicSensor: Boolean,
    val category: SensorCategory,
    val isAvailable: Boolean = true,
    val valueCount: Int = 3 // Number of values (X, Y, Z) or single value
) {
    companion object {
        fun fromSensor(sensor: Sensor, category: SensorCategory): SensorInfo {
            return SensorInfo(
                type = sensor.type,
                name = sensor.name,
                stringType = sensor.stringType,
                vendor = sensor.vendor,
                version = sensor.version,
                resolution = sensor.resolution,
                maximumRange = sensor.maximumRange,
                power = sensor.power,
                minDelay = sensor.minDelay,
                maxDelay = sensor.maxDelay,
                fifoMaxEventCount = sensor.fifoMaxEventCount,
                fifoReservedEventCount = sensor.fifoReservedEventCount,
                isWakeUpSensor = sensor.isWakeUpSensor,
                isDynamicSensor = sensor.isDynamicSensor,
                category = category,
                isAvailable = true,
                valueCount = getValueCount(sensor.type)
            )
        }

        private fun getValueCount(sensorType: Int): Int {
            return when (sensorType) {
                Sensor.TYPE_ACCELEROMETER,
                Sensor.TYPE_MAGNETIC_FIELD,
                Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_GRAVITY,
                Sensor.TYPE_LINEAR_ACCELERATION -> 3

                Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_GAME_ROTATION_VECTOR -> 4

                Sensor.TYPE_LIGHT,
                Sensor.TYPE_PRESSURE,
                Sensor.TYPE_PROXIMITY,
                Sensor.TYPE_AMBIENT_TEMPERATURE,
                Sensor.TYPE_RELATIVE_HUMIDITY,
                Sensor.TYPE_STEP_COUNTER,
                Sensor.TYPE_HEART_RATE -> 1

                else -> 3
            }
        }

        fun getDisplayName(sensorType: Int): String {
            return when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
                Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
                Sensor.TYPE_GYROSCOPE -> "Gyroscope"
                Sensor.TYPE_LIGHT -> "Light"
                Sensor.TYPE_PRESSURE -> "Pressure"
                Sensor.TYPE_PROXIMITY -> "Proximity"
                Sensor.TYPE_GRAVITY -> "Gravity"
                Sensor.TYPE_LINEAR_ACCELERATION -> "Linear Acceleration"
                Sensor.TYPE_ROTATION_VECTOR -> "Rotation Vector"
                Sensor.TYPE_RELATIVE_HUMIDITY -> "Humidity"
                Sensor.TYPE_AMBIENT_TEMPERATURE -> "Temperature"
                Sensor.TYPE_GAME_ROTATION_VECTOR -> "Game Rotation"
                Sensor.TYPE_STEP_COUNTER -> "Step Counter"
                Sensor.TYPE_STEP_DETECTOR -> "Step Detector"
                Sensor.TYPE_HEART_RATE -> "Heart Rate"
                Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant Motion"
                else -> "Unknown Sensor"
            }
        }
    }
}
