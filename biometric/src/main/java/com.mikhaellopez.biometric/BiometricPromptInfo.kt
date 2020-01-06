package com.mikhaellopez.biometric

data class BiometricPromptInfo(
    val title: String,
    val negativeButtonText: String,
    val subtitle: String? = null,
    val description: String? = null,
    val confirmationRequired: Boolean = true
)