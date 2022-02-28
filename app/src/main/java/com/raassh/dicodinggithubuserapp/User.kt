package com.raassh.dicodinggithubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val name: String,
    val location: String,
    val repositoryCount: Int,
    val company: String,
    val followersCount: Int,
    val followingCount: Int,
    val avatar: Int
) : Parcelable {
    override fun toString(): String {
        return """
            Github User
            Name: $name
            Username: $username
            Company: $company
            Location: $location
            Repositories: $repositoryCount
            Followers: $followersCount
            Following: $followingCount
        """.trimIndent()
    }
}
