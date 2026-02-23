package com.rejowan.sensors.data.model

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [SensorData] data class.
 */
class SensorDataTest {

    @Test
    fun `SensorData stores timestamp correctly`() {
        val timestamp = 123456789L
        val data = SensorData(timestamp, floatArrayOf(1f, 2f, 3f), 3)
        assertEquals(timestamp, data.timestamp)
    }

    @Test
    fun `SensorData stores values correctly`() {
        val values = floatArrayOf(1.5f, 2.5f, 3.5f)
        val data = SensorData(0L, values, 3)
        assertArrayEquals(values, data.values, 0.001f)
    }

    @Test
    fun `SensorData stores accuracy correctly`() {
        val accuracy = 2
        val data = SensorData(0L, floatArrayOf(1f), accuracy)
        assertEquals(accuracy, data.accuracy)
    }

    @Test
    fun `SensorData equals returns true for same values`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        assertTrue(data1 == data2)
    }

    @Test
    fun `SensorData equals returns false for different timestamps`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(200L, floatArrayOf(1f, 2f, 3f), 3)
        assertFalse(data1 == data2)
    }

    @Test
    fun `SensorData equals returns false for different values`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(100L, floatArrayOf(1f, 2f, 4f), 3)
        assertFalse(data1 == data2)
    }

    @Test
    fun `SensorData equals returns false for different accuracy`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 2)
        assertFalse(data1 == data2)
    }

    @Test
    fun `SensorData hashCode is same for equal objects`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        assertEquals(data1.hashCode(), data2.hashCode())
    }

    @Test
    fun `SensorData hashCode differs for different objects`() {
        val data1 = SensorData(100L, floatArrayOf(1f, 2f, 3f), 3)
        val data2 = SensorData(200L, floatArrayOf(4f, 5f, 6f), 1)
        assertNotEquals(data1.hashCode(), data2.hashCode())
    }

    @Test
    fun `SensorData can hold single value`() {
        val data = SensorData(0L, floatArrayOf(42f), 3)
        assertEquals(1, data.values.size)
        assertEquals(42f, data.values[0], 0.001f)
    }

    @Test
    fun `SensorData can hold multiple values`() {
        val values = floatArrayOf(1f, 2f, 3f, 4f, 5f)
        val data = SensorData(0L, values, 3)
        assertEquals(5, data.values.size)
    }
}
