package com.mikhaellopez.biometricsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.biometricsample.extensions.addFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeActivity(savedInstanceState)
    }

    private fun initializeActivity(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            addFragment(R.id.container, MainFragment.newInstance())
        }
    }

}