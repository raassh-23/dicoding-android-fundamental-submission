package com.raassh.dicodinggithubuserapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.raassh.dicodinggithubuserapp.data.UserItem

@Database(entities = [UserItem::class], version = 1)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) = INSTANCE ?: synchronized(FavoriteUserDatabase::class.java) {
                databaseBuilder(context.applicationContext,
                    FavoriteUserDatabase::class.java, "favorite_user_database")
                    .build()
            }.also { INSTANCE = it }
    }
}