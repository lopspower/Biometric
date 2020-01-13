package com.mikhaellopez.biometric

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * BiometricPromptInfo is a data class to simplify the creation of PromptInfo
 * from Biometric AndroidX.
 */
data class BiometricPromptInfo(
    val title: String,
    val negativeButtonText: String,
    val subtitle: String? = null,
    val description: String? = null,
    val confirmationRequired: Boolean = true
)