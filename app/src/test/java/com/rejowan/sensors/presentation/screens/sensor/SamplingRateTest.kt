package com.rejowan.sensors.presentation.screens.sensor

import android.hardware.SensorManager
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [SamplingRate] enum.
 */
class SamplingRateTest {

    @Test
    fun `NORMAL has correct value`() {
        assertEquals(SensorManager.SENSOR_DELAY_NORMAL, SamplingRate.NORMAL.value)
    }

    @Test
    fun `UI has correct value`() {
        assertEquals(SensorManager.SENSOR_DELAY_UI, SamplingRate.UI.value)
    }

    @Test
    fun `GAME has correct value`() {
        assertEquals(SensorManager.SENSOR_DELAY_GAME, SamplingRate.GAME.value)
    }

    @Test
    fun `FASTEST has correct value`() {
        assertEquals(SensorManager.SENSOR_DELAY_FASTEST, SamplingRate.FASTEST.value)
    }

    @Test
    fun `NORMAL has correct display name`() {
        assertEquals("Normal", SamplingRate.NORMAL.displayName)
    }

    @Test
    fun `UI has correct display name`() {
        assertEquals("UI", SamplingRate.UI.displayName)
    }

    @Test
    fun `GAME has correct display name`() {
        assertEquals("Game", SamplingRate.GAME.displayName)
    }

    @Test
    fun `FASTEST has correct display name`() {
        assertEquals("Fastest", SamplingRate.FASTEST.displayName)
    }

    @Test
    fun `SamplingRate has exactly 4 values`() {
        assertEquals(4, SamplingRate.entries.size)
    }

    @Test
    fun `SamplingRate values are in order of decreasing delay`() {
        val values = SamplingRate.entries.map { it.value }
        // NORMAL (3) > UI (2) > GAME (1) > FASTEST (0)
        assertEquals(listOf(3, 2, 1, 0), values)
    }
}
