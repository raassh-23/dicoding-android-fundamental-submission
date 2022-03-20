package com.raassh.dicodinggithubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raassh.dicodinggithubuserapp.data.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val userRepository = FavoriteUserRepository.getInstance(application)

    fun getFavorites() = userRepository.getAllFavorites()

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(application) as T
        }
    }
}