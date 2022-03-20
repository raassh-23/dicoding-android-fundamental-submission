package com.raassh.dicodinggithubuserapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raassh.dicodinggithubuserapp.data.UserItem
import com.raassh.dicodinggithubuserapp.data.UserRepository
import com.raassh.dicodinggithubuserapp.data.api.ApiConfig
import com.raassh.dicodinggithubuserapp.data.api.UserDetailResponse
import com.raassh.dicodinggithubuserapp.misc.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val username: String, application: Application) : ViewModel() {
    private val userRepository = UserRepository.getInstance(application)

    private val _user = MutableLiveData<UserDetailResponse>()
    val user: LiveData<UserDetailResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _canRetry = MutableLiveData<Boolean>()
    val canRetry: LiveData<Boolean> = _canRetry

    val isFavorite = userRepository.isFavorite(username)

    init {
        getUserDetail()
    }

    fun setCanRetry() {
        _canRetry.value = true
    }

    fun getUserDetail() {
        _isLoading.value = true
        _canRetry.value = false

        ApiConfig.getApiService().getUserDetail(username)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _user.value = response.body()
                    } else {
                        _error.value = Event("User details")
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = Event("User details")
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })

    }

    fun setFavorite(user: UserItem, isFavorite: Boolean) {
        if (isFavorite) {
            userRepository.insertFavorite(user)
        } else {
            userRepository.deleteFavorite(user)
        }
    }

    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    // Ref: https://medium.com/@lucasnrb/advanced-viewmodel-9cca1499addb
    @Suppress("UNCHECKED_CAST")
    class Factory(private val username: String, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserDetailViewModel(username, application) as T
        }
    }
}