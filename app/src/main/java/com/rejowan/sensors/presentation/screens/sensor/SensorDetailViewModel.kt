package com.rejowan.sensors.presentation.screens.sensor

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rejowan.sensors.data.model.SensorData
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.data.repository.SensorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SamplingRate(val value: Int, val displayName: String) {
    NORMAL(SensorManager.SENSOR_DELAY_NORMAL, "Normal"),
    UI(SensorManager.SENSOR_DELAY_UI, "UI"),
    GAME(SensorManager.SENSOR_DELAY_GAME, "Game"),
    FASTEST(SensorManager.SENSOR_DELAY_FASTEST, "Fastest")
}

class SensorDetailViewModel(
    private val sensorRepository: SensorRepository,
    private val sensorType: Int
) : ViewModel() {

    private val _sensorInfo = MutableStateFlow<SensorInfo?>(null)
    val sensorInfo: StateFlow<SensorInfo?> = _sensorInfo.asStateFlow()

    private val _currentData = MutableStateFlow<SensorData?>(null)
    val currentData: StateFlow<SensorData?> = _currentData.asStateFlow()

    private val _xHistory = MutableStateFlow<List<Float>>(emptyList())
    val xHistory: StateFlow<List<Float>> = _xHistory.asStateFlow()

    private val _yHistory = MutableStateFlow<List<Float>>(emptyList())
    val yHistory: StateFlow<List<Float>> = _yHistory.asStateFlow()

    private val _zHistory = MutableStateFlow<List<Float>>(emptyList())
    val zHistory: StateFlow<List<Float>> = _zHistory.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _samplingRate = MutableStateFlow(SamplingRate.UI)
    val samplingRate: StateFlow<SamplingRate> = _samplingRate.asStateFlow()

    // Statistics
    private val _minValues = MutableStateFlow(floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE))
    val minValues: StateFlow<FloatArray> = _minValues.asStateFlow()

    private val _maxValues = MutableStateFlow(floatArrayOf(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE))
    val maxValues: StateFlow<FloatArray> = _maxValues.asStateFlow()

    private val _sumValues = MutableStateFlow(floatArrayOf(0f, 0f, 0f))
    private val _sampleCount = MutableStateFlow(0)

    val avgValues: StateFlow<FloatArray>
        get() = MutableStateFlow(
            if (_sampleCount.value > 0) {
                floatArrayOf(
                    _sumValues.value[0] / _sampleCount.value,
                    _sumValues.value[1] / _sampleCount.value,
                    _sumValues.value[2] / _sampleCount.value
                )
            } else {
                floatArrayOf(0f, 0f, 0f)
            }
        )

    private var sensorJob: Job? = null
    private val maxHistorySize = 50

    init {
        loadSensorInfo()
        startMonitoring()
    }

    private fun loadSensorInfo() {
        _sensorInfo.value = sensorRepository.getSensorInfo(sensorType)
    }

    fun setSamplingRate(rate: SamplingRate) {
        _samplingRate.value = rate
        clearHistory()
        startMonitoring()
    }

    fun startMonitoring() {
        sensorJob?.cancel()
        _isPaused.value = false

        sensorJob = viewModelScope.launch {
            sensorRepository.observeSensorData(sensorType, _samplingRate.value.value)
                .collect { data ->
                    if (!_isPaused.value) {
                        _currentData.value = data
                        updateHistory(data)
                    }
                }
        }
    }

    fun pauseMonitoring() {
        _isPaused.value = true
    }

    fun resumeMonitoring() {
        _isPaused.value = false
    }

    fun togglePause() {
        if (_isPaused.value) {
            resumeMonitoring()
        } else {
            pauseMonitoring()
        }
    }

    private fun updateHistory(data: SensorData) {
        val values = data.values

        if (values.isNotEmpty()) {
            _xHistory.value = (_xHistory.value + values[0]).takeLast(maxHistorySize)
            updateStatistics(0, values[0])
        }
        if (values.size > 1) {
            _yHistory.value = (_yHistory.value + values[1]).takeLast(maxHistorySize)
            updateStatistics(1, values[1])
        }
        if (values.size > 2) {
            _zHistory.value = (_zHistory.value + values[2]).takeLast(maxHistorySize)
            updateStatistics(2, values[2])
        }

        _sampleCount.value++
    }

    private fun updateStatistics(index: Int, value: Float) {
        // Update min
        val currentMin = _minValues.value.copyOf()
        if (value < currentMin[index]) {
            currentMin[index] = value
            _minValues.value = currentMin
        }

        // Update max
        val currentMax = _maxValues.value.copyOf()
        if (value > currentMax[index]) {
            currentMax[index] = value
            _maxValues.value = currentMax
        }

        // Update sum for average calculation
        val currentSum = _sumValues.value.copyOf()
        currentSum[index] += value
        _sumValues.value = currentSum
    }

    fun clearHistory() {
        _xHistory.value = emptyList()
        _yHistory.value = emptyList()
        _zHistory.value = emptyList()
        resetStatistics()
    }

    private fun resetStatistics() {
        _minValues.value = floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
        _maxValues.value = floatArrayOf(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE)
        _sumValues.value = floatArrayOf(0f, 0f, 0f)
        _sampleCount.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        sensorJob?.cancel()
    }
}
