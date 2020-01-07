package com.mikhaellopez.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * CryptoHelper is an object to create a default [BiometricPrompt.CryptoObject]
 * This class is also used to check if one biometric must be enrolled.
 */
class CryptoHelper {

    companion object {
        private const val DEFAULT_BIOMETRIC_KEY = "default_biometric_key"
    }

    /**
     * Return true if one biometric must be enrolled else false
     *
     * @return [Boolean]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkOneBiometricMustBeEnrolled(): Boolean =
        try {
            generateKey()
            true
        } catch (ex: InvalidAlgorithmParameterException) {
            false
        }

    /**
     * Returns a default [BiometricPrompt.CryptoObject]
     *
     * @return [BiometricPrompt.CryptoObject]
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun cryptoObject(): BiometricPrompt.CryptoObject =
        BiometricPrompt.CryptoObject(initCipher(generateKey()))

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateKey(): KeyStore {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        keyStore.load(null)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                DEFAULT_BIOMETRIC_KEY,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
        )

        keyGenerator.generateKey()

        return keyStore
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initCipher(keyStore: KeyStore): Cipher {
        val cipher =
            Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyStore.load(null)
        val key = keyStore.getKey(DEFAULT_BIOMETRIC_KEY, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }

}
