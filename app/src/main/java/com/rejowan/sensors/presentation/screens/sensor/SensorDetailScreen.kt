package com.rejowan.sensors.presentation.screens.sensor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.presentation.components.MultiLineChartView
import com.rejowan.sensors.presentation.components.SensorInfoRow
import com.rejowan.sensors.presentation.theme.chartBlue
import com.rejowan.sensors.presentation.theme.chartGreen
import com.rejowan.sensors.presentation.theme.chartRed
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorDetailScreen(
    sensorType: Int,
    onBackClick: () -> Unit,
    viewModel: SensorDetailViewModel = koinViewModel { parametersOf(sensorType) }
) {
    val sensorInfo by viewModel.sensorInfo.collectAsState()
    val currentData by viewModel.currentData.collectAsState()
    val xHistory by viewModel.xHistory.collectAsState()
    val yHistory by viewModel.yHistory.collectAsState()
    val zHistory by viewModel.zHistory.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = sensorInfo?.let { SensorInfo.getDisplayName(it.type) } ?: "Sensor",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.togglePause() }) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Resume" else "Pause"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Live Values Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Live Values",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    currentData?.let { data ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            if (data.values.isNotEmpty()) {
                                ValueDisplay(
                                    label = "X",
                                    value = data.values[0],
                                    color = chartBlue
                                )
                            }
                            if (data.values.size > 1) {
                                ValueDisplay(
                                    label = "Y",
                                    value = data.values[1],
                                    color = chartRed
                                )
                            }
                            if (data.values.size > 2) {
                                ValueDisplay(
                                    label = "Z",
                                    value = data.values[2],
                                    color = chartGreen
                                )
                            }
                        }
                    } ?: Text(
                        text = "Waiting for data...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Real-time Chart",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MultiLineChartView(
                        xData = xHistory,
                        yData = yHistory,
                        zData = zHistory,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        xColor = chartBlue,
                        yColor = chartRed,
                        zColor = chartGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(label = "X", color = chartBlue)
                        LegendItem(label = "Y", color = chartRed)
                        LegendItem(label = "Z", color = chartGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sensor Info Card
            sensorInfo?.let { info ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Sensor Information",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SensorInfoRow(label = "Type", value = info.stringType)
                        HorizontalDivider()
                        SensorInfoRow(label = "Manufacturer", value = info.vendor)
                        HorizontalDivider()
                        SensorInfoRow(label = "Resolution", value = info.resolution.toString())
                        HorizontalDivider()
                        SensorInfoRow(label = "Max Range", value = info.maximumRange.toString())
                        HorizontalDivider()
                        SensorInfoRow(label = "Power", value = "${info.power} mA")
                        HorizontalDivider()
                        SensorInfoRow(label = "Min Delay", value = "${info.minDelay} µs")
                        HorizontalDivider()
                        SensorInfoRow(label = "Wake-up", value = if (info.isWakeUpSensor) "Yes" else "No")
                    }
                }
            }
        }
    }
}

@Composable
private fun ValueDisplay(
    label: String,
    value: Float,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
        Text(
            text = String.format("%.4f", value),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LegendItem(
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "━",
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = " $label",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
