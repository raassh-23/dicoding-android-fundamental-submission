package com.raassh.dicodinggithubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val username: String,
    val avatar: String
) : Parcelable
