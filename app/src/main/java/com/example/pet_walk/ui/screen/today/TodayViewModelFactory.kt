package com.withwalk.app.ui.screen.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.withwalk.app.data.Repository.TodayRepository

class TodayViewModelFactory(private val repository: TodayRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodayViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return TodayViewModel(repository) as T
        }
        throw IllegalStateException("TodayViewModel 못 찾음")
    }
}