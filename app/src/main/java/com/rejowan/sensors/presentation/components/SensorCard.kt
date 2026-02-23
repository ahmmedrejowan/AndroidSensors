package com.rejowan.sensors.presentation.components

import android.hardware.Sensor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rejowan.sensors.data.model.SensorInfo

@Composable
fun SensorCard(
    sensorInfo: SensorInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getSensorIcon(sensorInfo.type),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = SensorInfo.getDisplayName(sensorInfo.type),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = sensorInfo.vendor,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View sensor",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getSensorIcon(sensorType: Int): ImageVector {
    return when (sensorType) {
        Sensor.TYPE_ACCELEROMETER,
        Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> Icons.Default.Speed
        Sensor.TYPE_GYROSCOPE,
        Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> Icons.Default.RotateRight
        Sensor.TYPE_MAGNETIC_FIELD,
        Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> Icons.Default.Explore
        Sensor.TYPE_LIGHT -> Icons.Default.LightMode
        Sensor.TYPE_PRESSURE -> Icons.Default.Compress
        Sensor.TYPE_PROXIMITY -> Icons.Default.NearMe
        Sensor.TYPE_GRAVITY -> Icons.Default.FitnessCenter
        Sensor.TYPE_LINEAR_ACCELERATION -> Icons.Default.Speed
        Sensor.TYPE_ROTATION_VECTOR,
        Sensor.TYPE_GAME_ROTATION_VECTOR,
        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> Icons.Default.RotateRight
        Sensor.TYPE_RELATIVE_HUMIDITY -> Icons.Default.WaterDrop
        Sensor.TYPE_AMBIENT_TEMPERATURE -> Icons.Default.Thermostat
        Sensor.TYPE_STEP_COUNTER,
        Sensor.TYPE_STEP_DETECTOR -> Icons.AutoMirrored.Filled.DirectionsWalk
        Sensor.TYPE_HEART_RATE -> Icons.Default.Favorite
        Sensor.TYPE_SIGNIFICANT_MOTION,
        Sensor.TYPE_MOTION_DETECT,
        Sensor.TYPE_STATIONARY_DETECT -> Icons.Default.PhoneAndroid
        Sensor.TYPE_HINGE_ANGLE -> Icons.Default.PhoneAndroid
        Sensor.TYPE_POSE_6DOF,
        Sensor.TYPE_HEAD_TRACKER -> Icons.Default.Sensors
        else -> Icons.Default.Sensors
    }
}
