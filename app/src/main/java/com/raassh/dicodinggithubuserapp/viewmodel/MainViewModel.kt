package com.raassh.dicodinggithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raassh.dicodinggithubuserapp.api.ApiConfig
import com.raassh.dicodinggithubuserapp.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.api.SearchUserResponse
import com.raassh.dicodinggithubuserapp.misc.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _resultCount = MutableLiveData<Int>()
    val resultCount: LiveData<Int> = _resultCount

    private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
    val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private var lastQuery: String = emptyQuery

    init {
        searchUsers()
    }

    fun searchUsers() {
        searchUsers(lastQuery)
    }

    fun searchUsers(query: String) {
        lastQuery = query
        _isLoading.value = true

        ApiConfig.getApiService().getSearchedUsers(query)
            .enqueue(object : Callback<SearchUserResponse> {
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _resultCount.value = response.body()?.totalCount
                        _listUsers.value = response.body()?.items
                    } else {
                        _error.value = Event("Search results")
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = Event("Search results")
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
    }

    companion object {
        private const val TAG = "MainViewModel"
        const val emptyQuery = "\"\""
    }
}