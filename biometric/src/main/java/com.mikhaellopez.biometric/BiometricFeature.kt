package com.mikhaellopez.biometric

import android.content.pm.PackageManager
import android.os.Build

data class BiometricFeature(
    val packageManager: PackageManager,
    val feature: String,
    val minSdk: Int,
    val type: BiometricType
) {
    val isAvailable: Boolean by lazy {
        if (Build.VERSION.SDK_INT >= minSdk) {
            packageManager.hasSystemFeature(feature)
        } else {
            false
        }
    }
}