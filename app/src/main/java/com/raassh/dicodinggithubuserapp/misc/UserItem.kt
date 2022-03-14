package com.raassh.dicodinggithubuserapp.misc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val username: String,
    val avatar: String
) : Parcelable