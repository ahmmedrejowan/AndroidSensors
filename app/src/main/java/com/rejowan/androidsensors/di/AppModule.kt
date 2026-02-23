package com.rejowan.androidsensors.di

import com.rejowan.androidsensors.data.preferences.ThemePreferences
import com.rejowan.androidsensors.data.repository.SensorRepository
import com.rejowan.androidsensors.presentation.screens.home.HomeViewModel
import com.rejowan.androidsensors.presentation.screens.sensor.SensorDetailViewModel
import com.rejowan.androidsensors.presentation.screens.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Preferences
    single { ThemePreferences(androidContext()) }

    // Repository
    single { SensorRepository(androidContext()) }

    // ViewModels
    viewModel { HomeViewModel(get()) }
    viewModel { parameters -> SensorDetailViewModel(get(), parameters.get()) }
    viewModel { SettingsViewModel(get()) }
}
