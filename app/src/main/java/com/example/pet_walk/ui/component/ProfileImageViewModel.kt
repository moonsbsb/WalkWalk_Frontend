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
        "not_yet" to R.drawable.transparent
    )
    var SeletedProfile = mutableStateMapOf(
        "dog1" to false,
        "dog2" to false,
        "dog3" to false
    )

}