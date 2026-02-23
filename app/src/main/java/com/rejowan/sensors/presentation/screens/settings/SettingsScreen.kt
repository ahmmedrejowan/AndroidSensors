package com.rejowan.sensors.presentation.screens.settings

import android.content.Intent
import android.os.Build
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rejowan.licensy.LicenseContent
import com.rejowan.licensy.Licenses
import com.rejowan.licensy.LicensyInteractionMode
import com.rejowan.licensy.LicensyStyle
import com.rejowan.licensy.compose.LicensyDefaults
import com.rejowan.licensy.compose.LicensyList
import com.rejowan.sensors.BuildConfig
import com.rejowan.sensors.data.preferences.ThemeMode
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val dynamicColorEnabled by viewModel.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current

    var showChangelogSheet by remember { mutableStateOf(false) }
    var showPrivacyPolicySheet by remember { mutableStateOf(false) }
    var showLicensesSheet by remember { mutableStateOf(false) }
    var showCreatorSheet by remember { mutableStateOf(false) }
    var showAppLicenseSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
            // Appearance Section
            SectionTitle("Appearance")

            SettingsCard {
                SettingsToggleItem(
                    title = "Theme",
                    options = listOf("System", "Light", "Dark"),
                    selectedIndex = themeMode.ordinal,
                    onSelectionChange = { index ->
                        viewModel.setThemeMode(ThemeMode.entries[index])
                    }
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    HorizontalDivider()

                    SettingsSwitchItem(
                        title = "Dynamic Colors",
                        description = "Use colors from your wallpaper",
                        checked = dynamicColorEnabled,
                        onCheckedChange = { viewModel.setDynamicColorEnabled(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // About Section
            SectionTitle("About")

            SettingsCard {
                SettingsClickableItem(
                    title = "Version ${BuildConfig.VERSION_NAME}",
                    description = "View changelog",
                    onClick = { showChangelogSheet = true }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "Privacy Policy",
                    description = "View our privacy policy",
                    onClick = { showPrivacyPolicySheet = true }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "Open Source Licenses",
                    description = "View third-party libraries",
                    onClick = { showLicensesSheet = true }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "Creator",
                    description = "About the developer",
                    onClick = { showCreatorSheet = true }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "App License",
                    description = "GNU General Public License v3.0",
                    onClick = { showAppLicenseSheet = true }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "Contact",
                    description = "Get in touch with the developer",
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:kmrejowan@gmail.com".toUri()
                            putExtra(Intent.EXTRA_SUBJECT, "Android Sensors Feedback")
                        }
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider()

                SettingsClickableItem(
                    title = "GitHub Repository",
                    description = "View source code",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/ahmmedrejowan/AndroidSensors".toUri()
                        )
                        context.startActivity(intent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Changelog Bottom Sheet
    if (showChangelogSheet) {
        ModalBottomSheet(
            onDismissRequest = { showChangelogSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            ChangelogContent()
        }
    }

    // Privacy Policy Bottom Sheet
    if (showPrivacyPolicySheet) {
        ModalBottomSheet(
            onDismissRequest = { showPrivacyPolicySheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            PrivacyPolicyContent()
        }
    }

    // Open Source Licenses Bottom Sheet
    if (showLicensesSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLicensesSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            LicensesContent()
        }
    }

    // Creator Info Bottom Sheet
    if (showCreatorSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCreatorSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            CreatorContent()
        }
    }

    // App License Bottom Sheet
    if (showAppLicenseSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAppLicenseSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            AppLicenseContent()
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelectionChange(index) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangelogContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Changelog",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ChangelogVersionItem(
            version = BuildConfig.VERSION_NAME,
            date = "2026-02-23",
            changes = listOf(
                "Initial release of Android Sensors",
                "Real-time sensor monitoring with live charts",
                "Support for all device sensors",
                "Motion, Position, Environment sensor categories",
                "Detailed sensor information display",
                "Search and filter sensors",
                "Dark mode and dynamic color theming support",
                "Material 3 design with modern UI"
            )
        )
    }
}

@Composable
private fun ChangelogVersionItem(
    version: String,
    date: String,
    changes: List<String>
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Version $version",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        changes.forEach { change ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "• ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = change,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun PrivacyPolicyContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Your Privacy is Protected",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PrivacyHighlightItem("No internet connection required")
                PrivacyHighlightItem("No data collection or sharing")
                PrivacyHighlightItem("No analytics or tracking")
                PrivacyHighlightItem("100% offline operation")
            }
        }

        PrivacySection(
            title = "No Data Collection",
            content = "Android Sensors does not collect, store, transmit, or share any personal data. The app operates completely offline and does not require an internet connection."
        )

        PrivacySection(
            title = "Local Data Storage",
            content = "App preferences are stored exclusively on your device. This data never leaves your device and is not accessible to anyone except you."
        )

        PrivacySection(
            title = "Sensor Information",
            content = "Android Sensors reads sensor data from your device's hardware sensors. This data is processed locally for display purposes and never transmitted anywhere."
        )

        Text(
            text = "Last updated: February 23, 2026",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun PrivacyHighlightItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "✓ ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LicensesContent() {
    val licenses = remember {
        listOf(
            LicenseContent(
                title = "Jetpack Compose",
                author = "Google",
                license = Licenses.APACHE_2_0,
                url = "https://developer.android.com/jetpack/compose"
            ),
            LicenseContent(
                title = "Koin",
                author = "Kotzilla",
                license = Licenses.APACHE_2_0,
                url = "https://insert-koin.io/"
            ),
            LicenseContent(
                title = "MPAndroidChart",
                author = "Philipp Jahoda",
                license = Licenses.APACHE_2_0,
                url = "https://github.com/PhilJay/MPAndroidChart"
            ),
            LicenseContent(
                title = "Material Components",
                author = "Google",
                license = Licenses.APACHE_2_0,
                url = "https://github.com/material-components/material-components-android"
            ),
            LicenseContent(
                title = "Kotlin Coroutines",
                author = "JetBrains",
                license = Licenses.APACHE_2_0,
                url = "https://github.com/Kotlin/kotlinx.coroutines"
            ),
            LicenseContent(
                title = "AndroidX Libraries",
                author = "Google",
                license = Licenses.APACHE_2_0,
                url = "https://developer.android.com/jetpack/androidx"
            ),
            LicenseContent(
                title = "Licensy",
                author = "K M Rejowan Ahmmed",
                license = Licenses.APACHE_2_0,
                url = "https://github.com/ahmmedrejowan/Licensy"
            )
        )
    }

    val colors = LicensyDefaults.colors(
        primaryColor = MaterialTheme.colorScheme.onSurface,
        secondaryColor = MaterialTheme.colorScheme.onSurfaceVariant,
        linkColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        backgroundColorExpanded = MaterialTheme.colorScheme.surfaceContainerHigh,
        dividerColor = MaterialTheme.colorScheme.outlineVariant,
        iconTint = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Open Source Licenses",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LicensyList(
            licenses = licenses,
            style = LicensyStyle.CARD,
            interactionMode = LicensyInteractionMode.BOTTOM_SHEET,
            colors = colors
        )
    }
}

@Composable
private fun CreatorContent() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About the Creator",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "K M Rejowan Ahmmed",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Senior Android Developer",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Text(
            text = "Android Sensors was created to help users and developers explore and understand the various sensors available on Android devices. With real-time monitoring and detailed information, it provides insights into device hardware capabilities.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                CreatorLinkItem(
                    icon = "🌐",
                    label = "Website",
                    value = "rejowan.com",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://rejowan.com".toUri()
                        )
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                CreatorLinkItem(
                    icon = "📧",
                    label = "Email",
                    value = "kmrejowan@gmail.com",
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:kmrejowan@gmail.com".toUri()
                        }
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                CreatorLinkItem(
                    icon = "💼",
                    label = "GitHub",
                    value = "github.com/ahmmedrejowan",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/ahmmedrejowan".toUri()
                        )
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                CreatorLinkItem(
                    icon = "🔗",
                    label = "LinkedIn",
                    value = "linkedin.com/in/ahmmedrejowan",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://linkedin.com/in/ahmmedrejowan".toUri()
                        )
                        context.startActivity(intent)
                    }
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Made with ❤️ by ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "K M Rejowan Ahmmed",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CreatorLinkItem(
    icon: String,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AppLicenseContent() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "GNU General Public License v3.0",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = """
Android Sensors - Device Sensor Monitor
Copyright (C) 2026 K M Rejowan Ahmmed

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see the link below.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Key Terms",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LicenseTermItem("✓ Freedom to use the software for any purpose")
                LicenseTermItem("✓ Freedom to study and modify the source code")
                LicenseTermItem("✓ Freedom to distribute copies")
                LicenseTermItem("✓ Freedom to distribute modified versions")
                LicenseTermItem("✓ Derivative works must be open source under GPL v3.0")
            }
        }

        TextButton(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://www.gnu.org/licenses/gpl-3.0.en.html".toUri()
                )
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "View Full GPL v3.0 License",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun LicenseTermItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
