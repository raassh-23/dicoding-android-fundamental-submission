package com.raassh.dicodinggithubuserapp.misc

import android.view.View
import com.raassh.dicodinggithubuserapp.data.UserItem
import com.raassh.dicodinggithubuserapp.data.api.ListUsersResponse

fun visibility(visible: Boolean) = if (visible) {
    View.VISIBLE
} else {
    View.INVISIBLE
}

fun createUserArrayList(listUsers: List<ListUsersResponse>): ArrayList<UserItem> {
    val users = ArrayList<UserItem>()

    for (user in listUsers) {
        users.add(
            UserItem(
                user.login,
                user.avatarUrl
            )
        )
    }

    return users
}