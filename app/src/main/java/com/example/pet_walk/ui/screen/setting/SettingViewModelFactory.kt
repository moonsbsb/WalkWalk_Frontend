package com.withwalk.app.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.withwalk.app.data.Repository.SettingRepository

class SettingViewModelFactory (private val repository: SettingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(repository) as T
        }
        throw IllegalStateException("SettingViewModel 못 찾음")
    }
}