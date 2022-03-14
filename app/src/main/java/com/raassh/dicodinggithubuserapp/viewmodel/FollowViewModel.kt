package com.raassh.dicodinggithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raassh.dicodinggithubuserapp.api.ApiConfig
import com.raassh.dicodinggithubuserapp.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.misc.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel(private val username: String, private val type: Int) : ViewModel() {
    private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
    val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _canRetry = MutableLiveData<Boolean>()
    val canRetry: LiveData<Boolean> = _canRetry

    val followType: String
        get() = FOLLOW_TYPE[type]

    private val responseCallback: Callback<List<ListUsersResponse>>
        get() {
            return object : Callback<List<ListUsersResponse>> {
                override fun onResponse(
                    call: Call<List<ListUsersResponse>>,
                    response: Response<List<ListUsersResponse>>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listUsers.value = response.body()
                    } else {
                        _error.value = Event(followType)
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = Event(followType)
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            }
        }

    init {
        getFollowUsers()
    }

    fun setCanRetry() {
        _canRetry.value = true
    }

    fun getFollowUsers() {
        _isLoading.value = true
        _canRetry.value = false

        if (type == 0) {
            ApiConfig.getApiService().getUserFollowers(username)
                .enqueue(responseCallback)
        } else {
            ApiConfig.getApiService().getUserFollowing(username)
                .enqueue(responseCallback)
        }
    }

    companion object {
        private const val TAG = "FollowViewModel"
        private val FOLLOW_TYPE = arrayOf(
            "Followers",
            "Following"
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val username: String, private val type: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FollowViewModel(username, type) as T
        }
    }
}