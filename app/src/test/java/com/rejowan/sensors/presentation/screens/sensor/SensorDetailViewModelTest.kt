package com.rejowan.sensors.presentation.screens.sensor

import android.hardware.Sensor
import com.rejowan.sensors.data.model.SensorCategory
import com.rejowan.sensors.data.model.SensorData
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.data.repository.SensorRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [SensorDetailViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SensorDetailViewModelTest {

    private lateinit var viewModel: SensorDetailViewModel
    private lateinit var repository: SensorRepository
    private val testDispatcher = StandardTestDispatcher()

    private val testSensorType = Sensor.TYPE_ACCELEROMETER

    private val mockSensorInfo = SensorInfo(
        type = testSensorType,
        name = "Test Accelerometer",
        stringType = "android.sensor.accelerometer",
        vendor = "Test Vendor",
        version = 1,
        resolution = 0.001f,
        maximumRange = 39.2f,
        power = 0.5f,
        minDelay = 10000,
        maxDelay = 500000,
        fifoMaxEventCount = 0,
        fifoReservedEventCount = 0,
        isWakeUpSensor = false,
        isDynamicSensor = false,
        category = SensorCategory.MOTION
    )

    private val mockSensorData = SensorData(
        timestamp = System.nanoTime(),
        values = floatArrayOf(1.0f, 2.0f, 3.0f),
        accuracy = 3
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        every { repository.getSensorInfo(testSensorType) } returns mockSensorInfo
        every { repository.observeSensorData(testSensorType, any()) } returns flowOf(mockSensorData)
        viewModel = SensorDetailViewModel(repository, testSensorType)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has sensor info loaded`() = runTest {
        advanceUntilIdle()
        assertNotNull(viewModel.sensorInfo.value)
        assertEquals(mockSensorInfo, viewModel.sensorInfo.value)
    }

    @Test
    fun `initial state has isPaused false`() {
        assertFalse(viewModel.isPaused.value)
    }

    @Test
    fun `initial state has default sampling rate UI`() {
        assertEquals(SamplingRate.UI, viewModel.samplingRate.value)
    }

    @Test
    fun `initial state has empty history`() {
        assertEquals(emptyList<Float>(), viewModel.xHistory.value)
        assertEquals(emptyList<Float>(), viewModel.yHistory.value)
        assertEquals(emptyList<Float>(), viewModel.zHistory.value)
    }

    @Test
    fun `togglePause changes isPaused from false to true`() {
        assertFalse(viewModel.isPaused.value)
        viewModel.togglePause()
        assertTrue(viewModel.isPaused.value)
    }

    @Test
    fun `togglePause changes isPaused from true to false`() {
        viewModel.togglePause() // false -> true
        viewModel.togglePause() // true -> false
        assertFalse(viewModel.isPaused.value)
    }

    @Test
    fun `pauseMonitoring sets isPaused to true`() {
        viewModel.pauseMonitoring()
        assertTrue(viewModel.isPaused.value)
    }

    @Test
    fun `resumeMonitoring sets isPaused to false`() {
        viewModel.pauseMonitoring()
        viewModel.resumeMonitoring()
        assertFalse(viewModel.isPaused.value)
    }

    @Test
    fun `setSamplingRate updates sampling rate`() {
        viewModel.setSamplingRate(SamplingRate.GAME)
        assertEquals(SamplingRate.GAME, viewModel.samplingRate.value)
    }

    @Test
    fun `setSamplingRate clears history`() = runTest {
        // First collect some data
        advanceUntilIdle()

        // Change sampling rate
        viewModel.setSamplingRate(SamplingRate.FASTEST)

        // History should be cleared
        assertEquals(emptyList<Float>(), viewModel.xHistory.value)
        assertEquals(emptyList<Float>(), viewModel.yHistory.value)
        assertEquals(emptyList<Float>(), viewModel.zHistory.value)
    }

    @Test
    fun `clearHistory clears all history lists`() = runTest {
        advanceUntilIdle()
        viewModel.clearHistory()

        assertEquals(emptyList<Float>(), viewModel.xHistory.value)
        assertEquals(emptyList<Float>(), viewModel.yHistory.value)
        assertEquals(emptyList<Float>(), viewModel.zHistory.value)
    }

    @Test
    fun `clearHistory resets statistics`() = runTest {
        advanceUntilIdle()
        viewModel.clearHistory()

        val minValues = viewModel.minValues.value
        val maxValues = viewModel.maxValues.value

        assertEquals(Float.MAX_VALUE, minValues[0], 0.001f)
        assertEquals(Float.MIN_VALUE, maxValues[0], 0.001f)
    }

    @Test
    fun `sensor data is collected when not paused`() = runTest {
        advanceUntilIdle()
        assertNotNull(viewModel.currentData.value)
    }

    @Test
    fun `history updates with sensor data`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.xHistory.value.isNotEmpty())
    }
}
