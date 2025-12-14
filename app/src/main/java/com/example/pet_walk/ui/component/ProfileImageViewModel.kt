package com.withwalk.app.ui.component

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.withwalk.app.R

class ProfileImageViewModel: ViewModel() {

    val dogKind = mapOf(
        "dog1" to R.drawable.dog1,
        "dog2" to R.drawable.dog2,
        "dog3" to R.drawable.dog3,
        "dog4" to R.drawable.dog4,
        "dog5" to R.drawable.dog5,
        //"dog6" to R.drawable.dog6,
        "not_yet" to R.drawable.transparent
    )
    val runDogKind = mapOf(
        "dog1" to R.drawable.run_dog1,
        "dog2" to R.drawable.run_dog2,
        "dog3" to R.drawable.run_dog3,
        "dog4" to R.drawable.run_dog4,
        "dog5" to R.drawable.run_dog5,
        "dog6" to R.drawable.run_dog6,
        "not_yet" to R.drawable.transparent
    )
    val slowDogKind = mapOf(
        "dog1" to R.drawable.rest_dog1,
        "dog2" to R.drawable.rest_dog2,
        "dog3" to R.drawable.rest_dog3,
        "dog4" to R.drawable.rest_dog4,
        "dog5" to R.drawable.rest_dog5,
        "dog6" to R.drawable.rest_dog6,
        "not_yet" to R.drawable.transparent
    )
    var SeletedProfile = mutableStateMapOf(
        "dog1" to false,
        "dog2" to false,
        "dog3" to false,
        "dog4" to false,
        "dog5" to false,
        "dog6" to false
    )

}