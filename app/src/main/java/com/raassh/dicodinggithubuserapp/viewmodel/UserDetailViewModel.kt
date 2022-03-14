package com.raassh.dicodinggithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raassh.dicodinggithubuserapp.api.ApiConfig
import com.raassh.dicodinggithubuserapp.api.UserDetailResponse
import com.raassh.dicodinggithubuserapp.misc.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {
    private val _user = MutableLiveData<UserDetailResponse>()
    val user: LiveData<UserDetailResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    fun getUserDetail(username: String) {
        _isLoading.value = true

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

    companion object {
        private const val TAG = "UserDetailViewModel"
    }
}