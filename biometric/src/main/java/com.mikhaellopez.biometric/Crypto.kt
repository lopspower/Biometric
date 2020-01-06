package com.mikhaellopez.biometric

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class Crypto {

    companion object {
        private val DEFAULT_KEY_NAME = "default_key"
    }

    private val keyStore: KeyStore
    private val keyGenerator: KeyGenerator

    init {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
    }

    fun cryptoObject(): BiometricPrompt.CryptoObject {
        createKey(DEFAULT_KEY_NAME, false)

        val defaultCipher = Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )

        initCipher(defaultCipher,
            DEFAULT_KEY_NAME
        )

        return BiometricPrompt.CryptoObject(defaultCipher)
    }

    /**
     * Lifted code from the Google samples - https://github.com/googlesamples/android-FingerprintDialog/blob/master/kotlinApp/app/src/main/java/com/example/android/fingerprintdialog/MainActivity.kt
     *
     * Initialize the [Cipher] instance with the created key in the
     * [.createKey] method.
     *
     * @param keyName the key name to init the cipher
     * @return `true` if initialization is successful, `false` if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private fun initCipher(cipher: Cipher, keyName: String) {
        keyStore.load(null)
        val key = keyStore.getKey(keyName, null) as SecretKey
        cipher.init(Cipher.ENCRYPT_MODE, key)
    }

    /**
     * Lifted code from the Google Samples - https://github.com/googlesamples/android-FingerprintDialog/blob/master/kotlinApp/app/src/main/java/com/example/android/fingerprintdialog/MainActivity.kt
     *
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     *
     * @param keyName the name of the key to be created
     * @param invalidatedByBiometricEnrollment if `false` is passed, the created key will not
     * be invalidated even if a new fingerprint is enrolled.
     * The default value is `true`, so passing
     * `true` doesn't change the behavior
     * (the key will be invalidated if a new fingerprint is
     * enrolled.). Note that this parameter is only valid if
     * the app works on Android N developer preview.
     */
    private fun createKey(keyName: String, invalidatedByBiometricEnrollment: Boolean) {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        keyStore.load(null)
        // Set the alias of the entry in Android KeyStore where the key will appear
        // and the constrains (purposes) in the constructor of the Builder

        val builder = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            // Require the user to authenticate with a fingerprint to authorize every use
            // of the key
            .setUserAuthenticationRequired(true)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)

        // This is a workaround to avoid crashes on devices whose API level is < 24
        // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
        // visible on API level +24.
        // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
        // which isn't available yet.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment)
        }
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    }

}
