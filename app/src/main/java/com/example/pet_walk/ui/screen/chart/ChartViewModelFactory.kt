package com.withwalk.app.ui.screen.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.withwalk.app.data.Repository.ChartRepository

class ChartViewModelFactory (private val repository: ChartRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ChartViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ChartViewModel(repository) as T
        }
        throw IllegalStateException("ChartViewModel 못 찾음")
    }
}