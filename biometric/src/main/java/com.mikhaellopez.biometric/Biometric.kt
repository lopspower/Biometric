package com.mikhaellopez.biometric

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class Biometric(private val fragment: Fragment) {

    private val context: Context = fragment.context!!

    private val biometricManager: BiometricManager = BiometricManager.from(context)

    private val availableFeatures: List<BiometricType> =
        listOf(
            "android.hardware.fingerprint" to BiometricType.FINGERPRINT,
            "android.hardware.biometrics.face" to BiometricType.FACE,
            "android.hardware.biometrics.iris" to BiometricType.IRIS
        ).filter { context.packageManager.hasSystemFeature(it.first) }.map { it.second }

    fun getBiometricType(): BiometricType =
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> when {
                availableFeatures.isEmpty() -> BiometricType.NONE
                availableFeatures.size == 1 -> availableFeatures[0]
                else -> BiometricType.MULTIPLE
            }
            else -> BiometricType.NONE
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showBiometricPrompt(
        biometricPromptInfo: BiometricPromptInfo,
        crypto: BiometricPrompt.CryptoObject = Crypto().cryptoObject(),
        onError: ((Int, CharSequence) -> Unit)? = null,
        onFailed: (() -> Unit)? = null,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        val promptInfo = PromptInfo.Builder()
            .setTitle(biometricPromptInfo.title)
            .setNegativeButtonText(biometricPromptInfo.negativeButtonText)
            .setSubtitle(biometricPromptInfo.subtitle)
            .setDescription(biometricPromptInfo.description)
            .setConfirmationRequired(biometricPromptInfo.confirmationRequired)
            .build()
        showBiometricPrompt(
            promptInfo,
            getAuthenticationCallback(onSuccess, onError, onFailed),
            crypto
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showBiometricPrompt(
        biometricPromptInfo: BiometricPromptInfo,
        authenticationCallback: BiometricPrompt.AuthenticationCallback,
        crypto: BiometricPrompt.CryptoObject = Crypto().cryptoObject()
    ) {
        val promptInfo = PromptInfo.Builder()
            .setTitle(biometricPromptInfo.title)
            .setNegativeButtonText(biometricPromptInfo.negativeButtonText)
            .setSubtitle(biometricPromptInfo.subtitle)
            .setDescription(biometricPromptInfo.description)
            .setConfirmationRequired(biometricPromptInfo.confirmationRequired)
            .build()
        showBiometricPrompt(promptInfo, authenticationCallback, crypto)
    }

    fun showBiometricPrompt(
        promptInfo: PromptInfo,
        authenticationCallback: BiometricPrompt.AuthenticationCallback,
        crypto: BiometricPrompt.CryptoObject
    ) {
        BiometricPrompt(fragment, ContextCompat.getMainExecutor(context), authenticationCallback)
            .authenticate(promptInfo, crypto)
    }

    private fun getAuthenticationCallback(
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: ((Int, CharSequence) -> Unit)? = null,
        onFailed: (() -> Unit)? = null
    ): BiometricPrompt.AuthenticationCallback =
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess(result)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError?.invoke(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed?.invoke()
            }
        }

}