package com.mikhaellopez.biometricsample.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Adds a [Fragment] to this activity's layout.
 * @param containerViewId The container view to where add the fragment.
 * @param fragment The fragment to be added.
 */
fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(containerViewId, fragment).commit()
}