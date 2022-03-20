package com.raassh.dicodinggithubuserapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.raassh.dicodinggithubuserapp.data.UserItem

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserItem)

    @Delete
    fun delete(user: UserItem)

    @Query("SELECT * FROM favorite_user ORDER BY username DESC")
    fun getAllFavorites(): LiveData<List<UserItem>>

    @Query("SELECT EXISTS(SELECT username FROM favorite_user WHERE username = :username)")
    fun isFavorite(username: String): LiveData<Boolean>
}