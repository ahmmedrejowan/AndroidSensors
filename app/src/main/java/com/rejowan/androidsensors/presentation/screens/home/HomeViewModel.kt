package com.rejowan.androidsensors.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.rejowan.androidsensors.data.model.SensorCategory
import com.rejowan.androidsensors.data.model.SensorInfo
import com.rejowan.androidsensors.data.repository.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val sensorRepository: SensorRepository
) : ViewModel() {

    private val _sensorsByCategory = MutableStateFlow<Map<SensorCategory, List<SensorInfo>>>(emptyMap())
    val sensorsByCategory: StateFlow<Map<SensorCategory, List<SensorInfo>>> = _sensorsByCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadSensors()
    }

    private fun loadSensors() {
        _sensorsByCategory.value = sensorRepository.getSensorsByCategory()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredSensors(): Map<SensorCategory, List<SensorInfo>> {
        val query = _searchQuery.value.lowercase()
        if (query.isEmpty()) {
            return _sensorsByCategory.value
        }

        return _sensorsByCategory.value.mapValues { (_, sensors) ->
            sensors.filter { sensor ->
                sensor.name.lowercase().contains(query) ||
                SensorInfo.getDisplayName(sensor.type).lowercase().contains(query)
            }
        }.filterValues { it.isNotEmpty() }
    }
}
