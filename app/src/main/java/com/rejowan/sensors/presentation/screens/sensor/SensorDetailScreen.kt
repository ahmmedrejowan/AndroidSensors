package com.rejowan.sensors.presentation.screens.sensor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rejowan.sensors.data.model.SensorInfo
import com.rejowan.sensors.presentation.components.LineChartView
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
    val samplingRate by viewModel.samplingRate.collectAsState()
    val minValues by viewModel.minValues.collectAsState()
    val maxValues by viewModel.maxValues.collectAsState()
    val avgValues by viewModel.avgValues.collectAsState()

    var showSamplingMenu by remember { mutableStateOf(false) }

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
                    // Sampling rate selector
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("Sampling Rate: ${samplingRate.displayName}")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { showSamplingMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Sampling Rate"
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = showSamplingMenu,
                        onDismissRequest = { showSamplingMenu = false }
                    ) {
                        SamplingRate.entries.forEach { rate ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(rate.displayName)
                                        if (rate == samplingRate) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "✓",
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    viewModel.setSamplingRate(rate)
                                    showSamplingMenu = false
                                }
                            )
                        }
                    }

                    // Clear history
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("Clear History")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { viewModel.clearHistory() }) {
                            Icon(
                                imageVector = Icons.Default.ClearAll,
                                contentDescription = "Clear History"
                            )
                        }
                    }

                    // Pause/Resume
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(if (isPaused) "Resume" else "Pause")
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        IconButton(onClick = { viewModel.togglePause() }) {
                            Icon(
                                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isPaused) "Resume" else "Pause"
                            )
                        }
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
            val isSingleValue = sensorInfo?.let { SensorInfo.isSingleValue(it.type) } ?: false
            val unit = sensorInfo?.let { SensorInfo.getUnit(it.type) } ?: ""

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Live Values",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (unit.isNotEmpty()) {
                            Text(
                                text = "Unit: $unit",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    currentData?.let { data ->
                        if (isSingleValue) {
                            // Single value display (centered, larger)
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = String.format("%.2f", data.values.getOrNull(0) ?: 0f),
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = chartBlue
                                )
                                if (unit.isNotEmpty()) {
                                    Text(
                                        text = unit,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            // Multi-value display (X, Y, Z)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                if (data.values.isNotEmpty()) {
                                    ValueDisplay(
                                        label = "X",
                                        value = data.values[0],
                                        unit = unit,
                                        color = chartBlue
                                    )
                                }
                                if (data.values.size > 1) {
                                    ValueDisplay(
                                        label = "Y",
                                        value = data.values[1],
                                        unit = unit,
                                        color = chartRed
                                    )
                                }
                                if (data.values.size > 2) {
                                    ValueDisplay(
                                        label = "Z",
                                        value = data.values[2],
                                        unit = unit,
                                        color = chartGreen
                                    )
                                }
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

                    if (isSingleValue) {
                        // Single line chart for single-value sensors
                        LineChartView(
                            data = xHistory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            lineColor = chartBlue,
                            showFill = true
                        )
                    } else {
                        // Multi-line chart for multi-value sensors
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

                        // Legend (only for multi-value sensors)
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Card
            if (xHistory.isNotEmpty()) {
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
                            text = "Statistics",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isSingleValue) {
                            // Single value statistics
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatItem(
                                    label = "Min",
                                    value = if (minValues[0] != Float.MAX_VALUE) minValues[0] else 0f,
                                    unit = unit
                                )
                                StatItem(
                                    label = "Avg",
                                    value = avgValues[0],
                                    unit = unit
                                )
                                StatItem(
                                    label = "Max",
                                    value = if (maxValues[0] != Float.MIN_VALUE) maxValues[0] else 0f,
                                    unit = unit
                                )
                            }
                        } else {
                            // Multi-value statistics table
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Header column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("", style = MaterialTheme.typography.labelMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("X", style = MaterialTheme.typography.labelMedium, color = chartBlue)
                                    Text("Y", style = MaterialTheme.typography.labelMedium, color = chartRed)
                                    Text("Z", style = MaterialTheme.typography.labelMedium, color = chartGreen)
                                }
                                // Min column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Min", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(String.format("%.2f", if (minValues[0] != Float.MAX_VALUE) minValues[0] else 0f), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", if (minValues[1] != Float.MAX_VALUE) minValues[1] else 0f), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", if (minValues[2] != Float.MAX_VALUE) minValues[2] else 0f), style = MaterialTheme.typography.bodySmall)
                                }
                                // Avg column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Avg", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(String.format("%.2f", avgValues[0]), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", avgValues[1]), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", avgValues[2]), style = MaterialTheme.typography.bodySmall)
                                }
                                // Max column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Max", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(String.format("%.2f", if (maxValues[0] != Float.MIN_VALUE) maxValues[0] else 0f), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", if (maxValues[1] != Float.MIN_VALUE) maxValues[1] else 0f), style = MaterialTheme.typography.bodySmall)
                                    Text(String.format("%.2f", if (maxValues[2] != Float.MIN_VALUE) maxValues[2] else 0f), style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

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
    unit: String,
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
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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

@Composable
private fun StatItem(
    label: String,
    value: Float,
    unit: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = String.format("%.2f", value),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
