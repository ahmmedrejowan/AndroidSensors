package com.rejowan.sensors.data.model

import android.hardware.Sensor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [SensorInfo] companion object functions.
 */
class SensorInfoTest {

    @Test
    fun `getDisplayName returns correct name for accelerometer`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_ACCELEROMETER)
        assertEquals("Accelerometer", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for gyroscope`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_GYROSCOPE)
        assertEquals("Gyroscope", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for light sensor`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_LIGHT)
        assertEquals("Light", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for pressure sensor`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_PRESSURE)
        assertEquals("Pressure", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for proximity sensor`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_PROXIMITY)
        assertEquals("Proximity", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for magnetic field`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_MAGNETIC_FIELD)
        assertEquals("Magnetic Field", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for gravity sensor`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_GRAVITY)
        assertEquals("Gravity", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for rotation vector`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_ROTATION_VECTOR)
        assertEquals("Rotation Vector", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for step counter`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_STEP_COUNTER)
        assertEquals("Step Counter", displayName)
    }

    @Test
    fun `getDisplayName returns correct name for heart rate`() {
        val displayName = SensorInfo.getDisplayName(Sensor.TYPE_HEART_RATE)
        assertEquals("Heart Rate", displayName)
    }

    @Test
    fun `getDisplayName returns Sensor for unknown type`() {
        val displayName = SensorInfo.getDisplayName(999)
        assertEquals("Sensor", displayName)
    }

    @Test
    fun `getUnit returns m per s squared for accelerometer`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_ACCELEROMETER)
        assertEquals("m/s²", unit)
    }

    @Test
    fun `getUnit returns rad per s for gyroscope`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_GYROSCOPE)
        assertEquals("rad/s", unit)
    }

    @Test
    fun `getUnit returns lux for light sensor`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_LIGHT)
        assertEquals("lux", unit)
    }

    @Test
    fun `getUnit returns hPa for pressure sensor`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_PRESSURE)
        assertEquals("hPa", unit)
    }

    @Test
    fun `getUnit returns cm for proximity sensor`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_PROXIMITY)
        assertEquals("cm", unit)
    }

    @Test
    fun `getUnit returns microTesla for magnetic field`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_MAGNETIC_FIELD)
        assertEquals("μT", unit)
    }

    @Test
    fun `getUnit returns percent for humidity sensor`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_RELATIVE_HUMIDITY)
        assertEquals("%", unit)
    }

    @Test
    fun `getUnit returns celsius for temperature sensor`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_AMBIENT_TEMPERATURE)
        assertEquals("°C", unit)
    }

    @Test
    fun `getUnit returns steps for step counter`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_STEP_COUNTER)
        assertEquals("steps", unit)
    }

    @Test
    fun `getUnit returns bpm for heart rate`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_HEART_RATE)
        assertEquals("bpm", unit)
    }

    @Test
    fun `getUnit returns empty string for rotation vector`() {
        val unit = SensorInfo.getUnit(Sensor.TYPE_ROTATION_VECTOR)
        assertEquals("", unit)
    }

    @Test
    fun `isSingleValue returns true for light sensor`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_LIGHT))
    }

    @Test
    fun `isSingleValue returns true for pressure sensor`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_PRESSURE))
    }

    @Test
    fun `isSingleValue returns true for proximity sensor`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_PROXIMITY))
    }

    @Test
    fun `isSingleValue returns true for temperature sensor`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_AMBIENT_TEMPERATURE))
    }

    @Test
    fun `isSingleValue returns true for humidity sensor`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_RELATIVE_HUMIDITY))
    }

    @Test
    fun `isSingleValue returns true for step counter`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_STEP_COUNTER))
    }

    @Test
    fun `isSingleValue returns true for heart rate`() {
        assertTrue(SensorInfo.isSingleValue(Sensor.TYPE_HEART_RATE))
    }

    @Test
    fun `isSingleValue returns false for accelerometer`() {
        assertFalse(SensorInfo.isSingleValue(Sensor.TYPE_ACCELEROMETER))
    }

    @Test
    fun `isSingleValue returns false for gyroscope`() {
        assertFalse(SensorInfo.isSingleValue(Sensor.TYPE_GYROSCOPE))
    }

    @Test
    fun `isSingleValue returns false for magnetic field`() {
        assertFalse(SensorInfo.isSingleValue(Sensor.TYPE_MAGNETIC_FIELD))
    }

    @Test
    fun `isSingleValue returns false for rotation vector`() {
        assertFalse(SensorInfo.isSingleValue(Sensor.TYPE_ROTATION_VECTOR))
    }
}
