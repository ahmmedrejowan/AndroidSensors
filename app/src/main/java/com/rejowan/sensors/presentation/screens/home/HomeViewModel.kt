package com.rejowan.sensors.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.rejowan.sensors.data.model.SensorCategory
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.data.repository.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _showAllSensors = MutableStateFlow(false)
    val showAllSensors: StateFlow<Boolean> = _showAllSensors.asStateFlow()

    private val _sensorsByCategory = MutableStateFlow<Map<SensorCategory, List<SensorInfo>>>(emptyMap())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredSensors = MutableStateFlow<Map<SensorCategory, List<SensorInfo>>>(emptyMap())
    val filteredSensors: StateFlow<Map<SensorCategory, List<SensorInfo>>> = _filteredSensors.asStateFlow()

    private val _sensorCount = MutableStateFlow(0)
    val sensorCount: StateFlow<Int> = _sensorCount.asStateFlow()

    init {
        loadSensors()
    }

    private fun loadSensors() {
        _sensorsByCategory.value = if (_showAllSensors.value) {
            sensorRepository.getAllSensorsByCategory()
        } else {
            sensorRepository.getDefaultSensorsByCategory()
        }
        _sensorCount.value = _sensorsByCategory.value.values.sumOf { it.size }
        filterSensors(_searchQuery.value)
    }

    fun toggleShowAllSensors() {
        _showAllSensors.value = !_showAllSensors.value
        loadSensors()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterSensors(query)
    }

    private fun filterSensors(query: String) {
        val lowerQuery = query.lowercase()
        if (lowerQuery.isEmpty()) {
            _filteredSensors.value = _sensorsByCategory.value
            return
        }

        _filteredSensors.value = _sensorsByCategory.value.mapValues { (_, sensors) ->
            sensors.filter { sensor ->
                sensor.name.lowercase().contains(lowerQuery) ||
                SensorInfo.getDisplayName(sensor.type).lowercase().contains(lowerQuery)
            }
        }.filterValues { it.isNotEmpty() }
    }
}
