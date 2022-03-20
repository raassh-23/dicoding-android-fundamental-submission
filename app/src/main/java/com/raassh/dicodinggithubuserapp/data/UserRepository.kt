package com.raassh.dicodinggithubuserapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.raassh.dicodinggithubuserapp.data.db.FavoriteUserDao
import com.raassh.dicodinggithubuserapp.data.db.FavoriteUserDatabase
import java.util.concurrent.Executors

class UserRepository private constructor(application: Application) {
    private val favoriteUserDao: FavoriteUserDao
    private val executorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        favoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavorites() = favoriteUserDao.getAllFavorites()

    fun insertFavorite(userItem: UserItem) {
        executorService.execute { favoriteUserDao.insert(userItem) }
    }

    fun deleteFavorite(userItem: UserItem) {
        executorService.execute { favoriteUserDao.delete(userItem) }
    }

    fun isFavorite(username: String) = favoriteUserDao.isFavorite(username)

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(application: Application) = INSTANCE ?: synchronized(UserRepository::class.java) {
            UserRepository(application)
        }.also { INSTANCE = it }
    }
}