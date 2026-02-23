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

    private var sensorJob: Job? = null
    private val maxHistorySize = 50

    init {
        loadSensorInfo()
        startMonitoring()
    }

    private fun loadSensorInfo() {
        _sensorInfo.value = sensorRepository.getSensorInfo(sensorType)
    }

    fun startMonitoring() {
        sensorJob?.cancel()
        _isPaused.value = false

        sensorJob = viewModelScope.launch {
            sensorRepository.observeSensorData(sensorType, SensorManager.SENSOR_DELAY_UI)
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
        }
        if (values.size > 1) {
            _yHistory.value = (_yHistory.value + values[1]).takeLast(maxHistorySize)
        }
        if (values.size > 2) {
            _zHistory.value = (_zHistory.value + values[2]).takeLast(maxHistorySize)
        }
    }

    fun clearHistory() {
        _xHistory.value = emptyList()
        _yHistory.value = emptyList()
        _zHistory.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        sensorJob?.cancel()
    }
}
