package com.mikhaellopez.biometricsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.biometric.BiometricManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val biometricManager by lazy { BiometricManager(packageManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textBiometricType.text = "Biometric Type: ${biometricManager.getBiometricType()}"
    }

}