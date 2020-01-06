package com.mikhaellopez.biometricsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikhaellopez.biometric.Biometric
import com.mikhaellopez.biometric.BiometricPromptInfo
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private val biometricManager by lazy { Biometric(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textBiometricType.text = "Biometric Type: ${biometricManager.getBiometricType()}"

        btnPrompt.setOnClickListener {
            biometricManager.showBiometricPrompt(
                BiometricPromptInfo("Title", "Cancel")
            ) {
                // Do Something
            }
        }
    }

}