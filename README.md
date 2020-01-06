Biometric
=================

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
<br>
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)



<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

To used this usefull library you can grab it via Gradle:

```groovy
implementation 'com.mikhaellopez:biometric:1.0.0'
```

KOTLIN
-----

```kotlin
val biometricHelper = BiometricHelper(fragment)

// BiometricType = FACE, FINGERPRINT, IRIS, MULTIPLE or NONE
val biometricType: BiometricType = biometricHelper.getBiometricType()

btnStart.visibility = if (biometricHelper.biometricEnable()) View.VISIBLE else View.GONE

btnStart.setOnClickListener {
    biometricHelper.showBiometricPrompt(
        BiometricPromptInfo(
            title = "Title", // Mandatory
            negativeButtonText = "Cancel", // Mandatory
            subtitle = "Subtitle",
            description = "Description",
            confirmationRequired = true
        )
    ) {
        // Do something when success

    }
}
```

SUPPORT ❤️
-----

Find this library useful? Support it by joining [**stargazers**](https://github.com/lopspower/Biometric/stargazers) for this repository ⭐️
<br/>
And [**follow me**](https://github.com/lopspower?tab=followers) for my next creations 👍

LICENCE
-----

CircularImageView by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
