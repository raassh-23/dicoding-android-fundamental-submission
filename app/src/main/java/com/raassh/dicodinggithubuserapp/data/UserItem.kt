package com.raassh.dicodinggithubuserapp.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class UserItem(
    @ColumnInfo(name = "username")
    @PrimaryKey
    val username: String,

    @ColumnInfo(name = "avatar")
    val avatar: String
) : Parcelable {
    override fun toString(): String {
        return """
            Username: $username
            Url: https://github.com/$username/
        """.trimIndent()
    }
}