package com.mikhaellopez.biometric

import android.content.pm.PackageManager

class BiometricManager(private val packageManager: PackageManager) {

    fun getBiometricType(): BiometricType =
        availableFeatures().let {
            if (it.isNotEmpty()) {
                if (it.size > 1) {
                    BiometricType.MULTIPLE
                } else it[0].type
            } else BiometricType.UNKNOWN
        }

    fun availableFeatures(): List<BiometricFeature> = listOf(
        BiometricFeature(
            packageManager = packageManager,
            feature = "android.hardware.fingerprint", // PackageManager.FEATURE_FINGERPRINT
            minSdk = 23,
            type = BiometricType.FINGERPRINT
        ),
        BiometricFeature(
            packageManager = packageManager,
            feature = "android.hardware.biometrics.face", // PackageManager.FEATURE_FACE
            minSdk = 29,
            type = BiometricType.FACE
        ),
        BiometricFeature(
            packageManager = packageManager,
            feature = "android.hardware.biometrics.iris", // PackageManager.FEATURE_IRIS
            minSdk = 29,
            type = BiometricType.IRIS
        )
    ).filter { it.isAvailable }


}