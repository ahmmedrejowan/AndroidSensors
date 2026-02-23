package com.rejowan.sensors.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [SensorCategory] enum.
 */
class SensorCategoryTest {

    @Test
    fun `MOTION category has correct display name`() {
        assertEquals("Motion Sensors", SensorCategory.MOTION.displayName)
    }

    @Test
    fun `POSITION category has correct display name`() {
        assertEquals("Position Sensors", SensorCategory.POSITION.displayName)
    }

    @Test
    fun `ENVIRONMENT category has correct display name`() {
        assertEquals("Environment Sensors", SensorCategory.ENVIRONMENT.displayName)
    }

    @Test
    fun `OTHER category has correct display name`() {
        assertEquals("Other Sensors", SensorCategory.OTHER.displayName)
    }

    @Test
    fun `SensorCategory has exactly 4 values`() {
        assertEquals(4, SensorCategory.entries.size)
    }

    @Test
    fun `SensorCategory ordinal values are in expected order`() {
        assertEquals(0, SensorCategory.MOTION.ordinal)
        assertEquals(1, SensorCategory.POSITION.ordinal)
        assertEquals(2, SensorCategory.ENVIRONMENT.ordinal)
        assertEquals(3, SensorCategory.OTHER.ordinal)
    }
}
