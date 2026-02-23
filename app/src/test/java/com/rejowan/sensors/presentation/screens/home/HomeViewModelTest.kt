package com.rejowan.sensors.presentation.screens.home

import com.rejowan.sensors.data.model.SensorCategory
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.data.repository.SensorRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [HomeViewModel].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: SensorRepository
    private val testDispatcher = StandardTestDispatcher()

    private val mockSensorInfo = SensorInfo(
        type = 1,
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

    private val mockSensorsByCategory = mapOf(
        SensorCategory.MOTION to listOf(mockSensorInfo),
        SensorCategory.ENVIRONMENT to listOf(
            mockSensorInfo.copy(type = 5, name = "Test Light", category = SensorCategory.ENVIRONMENT)
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        every { repository.getDefaultSensorsByCategory() } returns mockSensorsByCategory
        every { repository.getAllSensorsByCategory() } returns mockSensorsByCategory
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty search query`() {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `initial state has showAllSensors false`() {
        assertFalse(viewModel.showAllSensors.value)
    }

    @Test
    fun `updateSearchQuery updates search query state`() {
        viewModel.updateSearchQuery("accel")
        assertEquals("accel", viewModel.searchQuery.value)
    }

    @Test
    fun `updateSearchQuery with empty string clears search`() {
        viewModel.updateSearchQuery("test")
        viewModel.updateSearchQuery("")
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `toggleShowAllSensors changes state from false to true`() {
        assertFalse(viewModel.showAllSensors.value)
        viewModel.toggleShowAllSensors()
        assertTrue(viewModel.showAllSensors.value)
    }

    @Test
    fun `toggleShowAllSensors changes state from true to false`() {
        viewModel.toggleShowAllSensors() // false -> true
        viewModel.toggleShowAllSensors() // true -> false
        assertFalse(viewModel.showAllSensors.value)
    }

    @Test
    fun `sensorCount reflects total number of sensors`() {
        assertEquals(2, viewModel.sensorCount.value)
    }

    @Test
    fun `filteredSensors returns all sensors when query is empty`() {
        viewModel.updateSearchQuery("")
        assertEquals(2, viewModel.filteredSensors.value.values.sumOf { it.size })
    }

    @Test
    fun `filteredSensors filters by sensor name`() {
        viewModel.updateSearchQuery("Light")
        val filtered = viewModel.filteredSensors.value
        assertEquals(1, filtered.values.sumOf { it.size })
    }

    @Test
    fun `filteredSensors is case insensitive`() {
        viewModel.updateSearchQuery("light")
        val filtered = viewModel.filteredSensors.value
        assertEquals(1, filtered.values.sumOf { it.size })
    }

    @Test
    fun `filteredSensors returns empty when no match`() {
        viewModel.updateSearchQuery("nonexistent")
        val filtered = viewModel.filteredSensors.value
        assertEquals(0, filtered.values.sumOf { it.size })
    }
}
