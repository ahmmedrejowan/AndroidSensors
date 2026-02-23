# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Koin
-keepnames class * implements org.koin.core.KoinComponent
-keep class org.koin.** { *; }

# MPAndroidChart
-keep class com.rejowan.chart.** { *; }
-dontwarn com.rejowan.chart.**

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Kotlin Serialization (if used)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# AndroidX DataStore
-keepclassmembers class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite {
    <fields>;
}

# Keep Compose runtime classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Licensy library
-keep class com.rejowan.licensy.** { *; }
