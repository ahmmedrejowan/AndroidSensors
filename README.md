# Android Sensors

<div align="center">
  <img src="https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/logo.webp" alt="Android Sensors Logo" width="120" height="120">

<h3>Real-time Device Sensor Monitor & Analyzer</h3>

  <p>
    A modern, feature-rich Android app for exploring and monitoring all device sensors with real-time data visualization and detailed statistics.
  </p>

[![Android](https://img.shields.io/badge/Platform-Android-green.svg?style=flat)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-blue.svg)](https://developer.android.com/jetpack/compose)
</div>

---

## Features

- **Complete Sensor Discovery** - Detect and list all available sensors on your device
- **Real-time Monitoring** - Live sensor data with configurable sampling rates
- **Interactive Charts** - Visual graphs showing sensor value trends over time
- **Live Statistics** - Min, Max, and Average values for each sensor axis
- **Sensor Categories** - Organized by Motion, Position, Environment, and Other
- **Detailed Information** - View sensor specifications (resolution, range, power, vendor)
- **Search & Filter** - Quickly find sensors by name or type
- **Sampling Rate Control** - Choose between Normal, UI, Game, and Fastest modes
- **Material 3 Design** - Modern UI with dark mode and dynamic colors
- **100% Offline** - Complete privacy, no internet required

---

## Download

![GitHub Release](https://img.shields.io/github/v/release/ahmmedrejowan/AndroidSensors)

You can download the latest APK from here

<a href="https://github.com/ahmmedrejowan/AndroidSensors/releases/download/1.0.0/Android_Sensors_1.0.0.apk">
<img src="https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/get.png" width="224px" align="center"/>
</a>

Check out the [releases](https://github.com/ahmmedrejowan/AndroidSensors/releases) section for more details.

---

## Screenshots

| Shots                                                                                                 | Shots                                                                                                 | Shots                                                                                                 |
|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| ![Screenshot 1](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot1.webp) | ![Screenshot 2](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot2.webp) | ![Screenshot 3](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot3.webp) |
| ![Screenshot 4](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot4.webp) | ![Screenshot 5](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot5.webp) | ![Screenshot 6](https://raw.githubusercontent.com/ahmmedrejowan/AndroidSensors/master/files/shot6.webp) |

---

## Architecture

Android Sensors follows **MVVM** architecture with **Repository** pattern:

```
app/
├── data/                      # Data layer
│   ├── model/                 # Data models (SensorInfo, SensorData, SensorCategory)
│   ├── preferences/           # DataStore preferences
│   └── repository/            # Sensor repository
│
├── presentation/              # Presentation layer (UI)
│   ├── components/            # Reusable Compose components
│   │   ├── SensorCard.kt      # Sensor list item
│   │   ├── LineChartView.kt   # Chart components
│   │   └── SensorInfoRow.kt   # Info display row
│   ├── navigation/            # Navigation graph
│   ├── screens/               # UI screens
│   │   ├── home/              # Home screen with sensor list
│   │   ├── sensor/            # Sensor detail screen
│   │   └── settings/          # Settings screen
│   └── theme/                 # Material 3 theming
│
└── di/                        # Koin dependency injection
```

### Tech Stack

- **UI Framework**: Jetpack Compose (100% Compose UI)
- **Language**: Kotlin (100%)
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Koin
- **Async**: Kotlin Coroutines + StateFlow
- **Navigation**: Jetpack Navigation Compose
- **Charts**: MPAndroidChart Reworked
- **Data Storage**: DataStore Preferences
- **Sensors**: Android SensorManager API

---

## Requirements

- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 36 (Android 15)
- **Compile SDK**: API 36
- **Gradle**: 8.13
- **AGP**: 8.13.2
- **Kotlin**: 2.3.10
- **Java**: 17

### Permissions

- `BODY_SENSORS` - Access heart rate and body sensors
- `ACTIVITY_RECOGNITION` - Access step counter and detector
- `HIGH_SAMPLING_RATE_SENSORS` - Enable fastest sampling rate

**Note:** This app is 100% offline and does not require internet permission.

---

## Build & Run

To build and run the project, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/ahmmedrejowan/AndroidSensors.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Connect your Android device or start an emulator.
5. Click on the "Run" button in Android Studio to build and run the app.

---

## Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

**Note**: Test coverage is currently being developed and will be added in future releases.

---

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Guidelines

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Follow the existing code style and architecture
4. Add tests for new features (when test infrastructure is ready)
5. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
6. Push to the branch (`git push origin feature/AmazingFeature`)
7. Open a Pull Request

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused
- Follow MVVM architecture principles

---

## License

```
Copyright (C) 2026 K M Rejowan Ahmmed

This program is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as
published by the Free Software Foundation, either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program. If not,
see <https://www.gnu.org/licenses/>.
```

> [!WARNING]
> **This is a copyleft license.** Android Sensors is licensed under GPL v3.0, which means:
> - You can freely use, modify, and distribute this software
> - Any derivative works **must also be licensed under GPL v3.0**
> - You **must disclose your source code** if you distribute modified versions
> - You **cannot distribute proprietary/closed-source versions** of this software
>
> If you need different licensing terms, please contact the author.

---

## Author

**K M Rejowan Ahmmed**

- GitHub: [@ahmmedrejowan](https://github.com/ahmmedrejowan)
- Email: [ahmmadrejowan@gmail.com](mailto:ahmmadrejowan@gmail.com)

---

## Acknowledgments

- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Charting library
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit
- [Material Design 3](https://m3.material.io/) - Design system
- [Koin](https://insert-koin.io/) - Dependency injection framework
- [Licensy](https://github.com/ahmmedrejowan/Licensy) - Open source licenses display

---

## Changelog

### v1.0.0 (2026-02-23) - Initial Release

- Complete sensor discovery and listing
- Real-time sensor monitoring with live values
- Interactive charts with multi-axis support
- Min/Max/Average statistics tracking
- Configurable sampling rates (Normal, UI, Game, Fastest)
- Sensor search and filtering
- "Show all sensors" toggle for complete sensor list
- Detailed sensor information display
- Material 3 design with dark mode and dynamic colors
- 100% offline functionality

---
