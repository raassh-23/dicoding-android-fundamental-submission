package com.raassh.dicodinggithubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raassh.dicodinggithubuserapp.api.*
import com.raassh.dicodinggithubuserapp.misc.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
    val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

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
                        _errorMessage.value = Event(response.message())
                    }
                }

                override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _errorMessage.value = Event(t.message as String)
                }

            }
        }

    fun getFollowers(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(responseCallback)
    }

    fun getFollowing(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(responseCallback)
    }
}