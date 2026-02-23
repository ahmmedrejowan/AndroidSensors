package com.rejowan.sensors

import android.content.Context
import android.hardware.SensorManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for Android Sensors app.
 * These tests run on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var appContext: Context
    private lateinit var sensorManager: SensorManager

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        sensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Test
    fun useAppContext() {
        assertEquals("com.rejowan.sensors", appContext.packageName)
    }

    @Test
    fun sensorManagerIsAvailable() {
        assertNotNull(sensorManager)
    }

    @Test
    fun deviceHasAtLeastOneSensor() {
        val sensors = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL)
        assertTrue("Device should have at least one sensor", sensors.isNotEmpty())
    }

    @Test
    fun appNameIsCorrect() {
        val appName = appContext.getString(R.string.app_name)
        assertEquals("Android Sensors", appName)
    }
}
