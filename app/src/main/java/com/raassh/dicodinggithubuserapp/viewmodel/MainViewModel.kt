package com.raassh.dicodinggithubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.raassh.dicodinggithubuserapp.data.api.ApiConfig
import com.raassh.dicodinggithubuserapp.data.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.data.api.SearchUserResponse
import com.raassh.dicodinggithubuserapp.misc.Event
import com.raassh.dicodinggithubuserapp.misc.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    private val _resultCount = MutableLiveData<Int>()
    val resultCount: LiveData<Int> = _resultCount

    private val _listUsers = MutableLiveData<List<ListUsersResponse>>()
    val listUsers: LiveData<List<ListUsersResponse>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _canRetry = MutableLiveData<Boolean>()
    val canRetry: LiveData<Boolean> = _canRetry

    private var lastQuery: String = emptyQuery

    init {
        searchUsers()
    }

    fun searchUsers() {
        searchUsers(lastQuery)
    }

    fun setCanRetry() {
        _canRetry.value = true
    }

    fun searchUsers(query: String) {
        lastQuery = query
        _isLoading.value = true
        _canRetry.value = false

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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val pref: SettingPreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(pref) as T
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        const val emptyQuery = "\"\""
    }
}