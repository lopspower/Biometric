package com.mikhaellopez.biometric

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class BiometricHelper(private val fragment: Fragment) {

    private val context: Context = fragment.context!!
    private val biometricManager: BiometricManager = BiometricManager.from(context)
    private val cryptoHelper by lazy { CryptoHelper() }

    private val availableFeatures: List<BiometricType> =
        listOf(
            "android.hardware.fingerprint" to BiometricType.FINGERPRINT,
            "android.hardware.biometrics.face" to BiometricType.FACE,
            "android.hardware.biometrics.iris" to BiometricType.IRIS
        ).filter { context.packageManager.hasSystemFeature(it.first) }.map { it.second }

    fun getBiometricType(): BiometricType =
        if (checkMinVersion() && cryptoHelper.checkOneBiometricMustBeEnrolled()) {
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS,
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> when {
                    availableFeatures.isEmpty() -> BiometricType.NONE
                    availableFeatures.size == 1 -> availableFeatures[0]
                    else -> BiometricType.MULTIPLE
                }
                else -> BiometricType.NONE
            }
        } else BiometricType.NONE

    fun biometricEnable(): Boolean = checkMinVersion() && getBiometricType() != BiometricType.NONE

    fun showBiometricPrompt(
        biometricPromptInfo: BiometricPromptInfo,
        onError: ((Int, CharSequence) -> Unit)? = null,
        onFailed: (() -> Unit)? = null,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        showBiometricPrompt(
            biometricPromptInfo.toPromptInfo(),
            getAuthenticationCallback(onSuccess, onError, onFailed)
        )
    }

    fun showBiometricPrompt(
        biometricPromptInfo: BiometricPromptInfo,
        crypto: BiometricPrompt.CryptoObject,
        onError: ((Int, CharSequence) -> Unit)? = null,
        onFailed: (() -> Unit)? = null,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        showBiometricPrompt(
            biometricPromptInfo.toPromptInfo(),
            getAuthenticationCallback(onSuccess, onError, onFailed),
            crypto
        )
    }

    fun showBiometricPrompt(
        biometricPromptInfo: BiometricPromptInfo,
        authenticationCallback: BiometricPrompt.AuthenticationCallback,
        crypto: BiometricPrompt.CryptoObject? = null
    ) {
        showBiometricPrompt(biometricPromptInfo.toPromptInfo(), authenticationCallback, crypto)
    }

    fun showBiometricPrompt(
        promptInfo: PromptInfo,
        crypto: BiometricPrompt.CryptoObject? = null,
        onError: ((Int, CharSequence) -> Unit)? = null,
        onFailed: (() -> Unit)? = null,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ) {
        showBiometricPrompt(
            promptInfo,
            getAuthenticationCallback(onSuccess, onError, onFailed),
            crypto
        )
    }

    fun showBiometricPrompt(
        promptInfo: PromptInfo,
        authenticationCallback: BiometricPrompt.AuthenticationCallback,
        crypto: BiometricPrompt.CryptoObject? = null
    ) {
        if (biometricEnable()) {
            BiometricPrompt(
                fragment,
                ContextCompat.getMainExecutor(context),
                authenticationCallback
            ).authenticate(promptInfo, crypto ?: cryptoHelper.cryptoObject())
        }
    }

    private fun checkMinVersion(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun BiometricPromptInfo.toPromptInfo(): PromptInfo =
        PromptInfo.Builder()
            .setTitle(title)
            .setNegativeButtonText(negativeButtonText)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setConfirmationRequired(confirmationRequired)
            .build()

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