package com.rejowan.sensors.data.preferences

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [ThemeMode] enum.
 */
class ThemeModeTest {

    @Test
    fun `ThemeMode has exactly 3 values`() {
        assertEquals(3, ThemeMode.entries.size)
    }

    @Test
    fun `ThemeMode SYSTEM has ordinal 0`() {
        assertEquals(0, ThemeMode.SYSTEM.ordinal)
    }

    @Test
    fun `ThemeMode LIGHT has ordinal 1`() {
        assertEquals(1, ThemeMode.LIGHT.ordinal)
    }

    @Test
    fun `ThemeMode DARK has ordinal 2`() {
        assertEquals(2, ThemeMode.DARK.ordinal)
    }

    @Test
    fun `ThemeMode can be converted to and from string`() {
        ThemeMode.entries.forEach { mode ->
            val name = mode.name
            val restored = ThemeMode.valueOf(name)
            assertEquals(mode, restored)
        }
    }

    @Test
    fun `ThemeMode entries are in expected order`() {
        val expected = listOf(ThemeMode.SYSTEM, ThemeMode.LIGHT, ThemeMode.DARK)
        assertEquals(expected, ThemeMode.entries.toList())
    }
}
