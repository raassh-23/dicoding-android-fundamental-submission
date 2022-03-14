package com.raassh.dicodinggithubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raassh.dicodinggithubuserapp.misc.Event
import com.raassh.dicodinggithubuserapp.api.ListUsersResponseItem
import com.raassh.dicodinggithubuserapp.api.SearchUserResponse
import com.raassh.dicodinggithubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _resultCount = MutableLiveData<Int>()
    val resultCount: LiveData<Int> = _resultCount

    private val _listUsers = MutableLiveData<List<ListUsersResponseItem>>()
    val listUsers: LiveData<List<ListUsersResponseItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    init {
        searchUsers(emptyQuery) // search empty query to show all user
    }

    fun searchUsers(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearchedUsers(query)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _resultCount.value = response.body()?.totalCount
                    _listUsers.value = response.body()?.items
                } else {
                    _errorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = Event(t.message as String)
            }

        })
    }

    companion object {
        private const val TAG = "MainViewModel"
        const val emptyQuery = "\"\""
    }
}