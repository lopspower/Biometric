<p align="center"><img src="/preview/header.png"></p>

Biometric
=================

<img src="/preview/face.gif" alt="sample" title="sample" width="250" height="509" align="right" />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Download](https://api.bintray.com/packages/lopspower/maven/com.mikhaellopez:biometric/images/download.svg?version=1.0.1)](https://bintray.com/lopspower/maven/com.mikhaellopez:biometric/1.0.1/link)
<br>
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d86db87d913f4ba4910fcc26a26409ef)](https://www.codacy.com/manual/lopspower/Biometric?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=lopspower/Biometric&amp;utm_campaign=Badge_Grade)

The easiest way is to use the new version of [Biometric](https://developer.android.com/jetpack/androidx/releases/biometric) under [AndroidX](https://developer.android.com/jetpack/androidx).

You can easily know if the device allows you to use <b>Biometric</b> and under what type (FACE, FINGERPRINT, IRIS, MULTIPLE...). The library also provides a [CryptoObject](https://developer.android.com/reference/android/hardware/biometrics/BiometricPrompt.CryptoObject) implementation if you want to implement it on your side on [CryptoHelper](/biometric/src/main/java/com.mikhaellopez.biometric/CryptoHelper.kt).

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

To used this usefull library you can grab it via Gradle:

```groovy
implementation 'com.mikhaellopez:biometric:1.0.1'
```

KOTLIN
-----

```kotlin
val biometricHelper = BiometricHelper(fragment)

// BiometricType = FACE, FINGERPRINT, IRIS, MULTIPLE or NONE
val biometricType: BiometricType = biometricHelper.getBiometricType()

// Check if biometric is available on the device
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

You can also handle error and failed like this:

```kotlin
biometricHelper.showBiometricPrompt(promptInfo,
    onError = { errorCode: Int, errString: CharSequence ->
        // Do something when error
                    
    }, onFailed = {
        // Do something when failed
                    
    }, onSuccess = { result: BiometricPrompt.AuthenticationResult ->
        // Do something when success
                    
    })
```

DEMO
-----

| Dark Mode                                                                                 | Biometric Fingerprint                                                                                   | Old Fingerprint                                                                                         |
| ----------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------- |
| <img src="/preview/face_dark.gif" alt="sample" title="sample" width="250" height="509" /> | <img src="/preview/finger_new.gif" alt="sample" title="sample" width="250" height="486" align="left" /> | <img src="/preview/finger_old.gif" alt="sample" title="sample" width="250" height="430" align="left" /> |

SUPPORT ❤️
-----

Find this library useful? Support it by joining [**stargazers**](https://github.com/lopspower/Biometric/stargazers) for this repository ⭐️
<br/>
And [**follow me**](https://github.com/lopspower?tab=followers) for my next creations 👍

LICENCE
-----

Biometric by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
